package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class MainTest {
    static String enginePackage = "org.example.engine";
    static String modelsPackage = "org.example.models";
    static String exceptionsPackage = "org.example.exceptions";
    static String engine = enginePackage + '.' + "Engine";
    static String board = modelsPackage + '.' + "Board";
    static String cell = modelsPackage + '.' + "Cell";
    static String piece = modelsPackage + '.' + "Piece";
    static String rook = modelsPackage + '.' + "Rook";
    static String knight = modelsPackage + '.' + "Knight";
    static String bishop = modelsPackage + '.' + "Bishop";
    static String queen = modelsPackage + '.' + "Queen";
    static String king = modelsPackage + '.' + "King";
    static String pawn = modelsPackage + '.' + "Pawn";
    static String point = modelsPackage + '.' + "Point";
    static String invalidMovement = exceptionsPackage + '.' + "InvalidMovementException";


    @Test
    @Order(1)
    @DisplayName("Douse Engine Exist?")
    void testEngineExist(){
        assertDoesNotThrow(() -> Class.forName(engine), engine + " douse not Exist!");
    }


    @ParameterizedTest
    @MethodSource("getModels")
    @Order(2)
    @DisplayName("Do All Models Exist?")
    void testModelsExist(String model){
        assertDoesNotThrow(() -> Class.forName(model), "Model %s douse not exist!".formatted(model));
    }

    static Stream<String> getModels() {
        return  Stream.of(board, cell, piece, rook, knight, bishop, queen, king, pawn, point);
    }

    @Test
    @Order(3)
    @DisplayName("Douse Exceptions Exist?")
    void testExceptionsExists(){
        assertDoesNotThrow(() -> Class.forName(invalidMovement), invalidMovement + " douse not exist!");
    }

    @Nested
    @Order(4)
    class EngineTest {
        @Test
        @DisplayName("Test Engine Default Constructor Exist")
        void testEngineDefaultConstructorExist() throws ClassNotFoundException {
            Class<?> engineClass = Class.forName(engine);
            assertDoesNotThrow(() -> engineClass.getConstructor(), "Engine has no default constructor");
        }

        @Test
        @DisplayName("Test Engine Board Exist")
        void testEngineBoardExist() throws ClassNotFoundException, NoSuchFieldException {
            Class<?> engineClass = Class.forName(engine);
            assertDoesNotThrow(() -> engineClass.getDeclaredField("board"), "Engine has no field board");
            Field engBoard = engineClass.getDeclaredField("board");
            assertEquals(engBoard.getType(), Class.forName(board), "Field board has false type");
        }

        @Test
        @DisplayName("Test Engine create Method Exist")
        void testEngineCreateMethodExist() throws ClassNotFoundException, NoSuchMethodException {
            Class<?> engineClass = Class.forName(engine);
            assertDoesNotThrow(() -> engineClass.getDeclaredMethod("createGame"), "Engine has no method createGame");
            Method createM = engineClass.getMethod("createGame");
            assertEquals(createM.getReturnType(), Void.TYPE);
        }

        @Test
        @DisplayName("create Method Test")
        void createTest() throws ReflectiveOperationException {
            Class<?> engClass = Class.forName(engine);
            Object eng = engClass.getConstructor().newInstance();
            engClass.getMethod("createGame").invoke(eng);
            Object brd = engClass.getDeclaredField("board").get(eng);
            assertNotNull(brd, "Engine createGame Method should initialize board!");
            Object[][] cells = (Object[][]) Class.forName(board).getField("cells").get(brd);
            assertNotNull(cells, "Engine createGame Method should call Board.build!");
        }
    }

    @Nested
    @Order(5)
    class ModelsTest {

        @Nested
        @Order(0)
        class BoardTest {


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(board);
                assertDoesNotThrow(() -> classObj.getConstructor(), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Fields Exist")
            void testFieldsExist() throws ClassNotFoundException, NoSuchFieldException {
                Class<?> classObj = Class.forName(board);

                assertDoesNotThrow(() -> classObj.getDeclaredField("cells"), "Class has no field cells");
                Field cells = classObj.getDeclaredField("cells");
                assertEquals(cells.getType(), Class.forName("[[L"+cell+';'), "Field cells should be cell[][]");
                assertTrue(Modifier.isPublic(cells.getModifiers()), "cells should be public");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(board);

                assertDoesNotThrow(() -> classObj.getMethod("build"), "Class has no method build");
                Method build1 = classObj.getDeclaredMethod("build");
                assertEquals(build1.getReturnType(), Void.TYPE, "build should return null");
                assertTrue(Modifier.isPublic(build1.getModifiers()), "build should be public");
            }


            @Nested
            class BuildTest{
                static Object[][] cells;

                @BeforeAll
                static void init() throws ReflectiveOperationException{
                    Class<?> classObj = Class.forName(board);
                    Object inst = classObj.getConstructor().newInstance();
                    classObj.getMethod("build").invoke(inst);
                    cells = (Object[][]) Class.forName(board).getField("cells").get(inst);
                }

                @Test
                @DisplayName("build Method Test")
                void buildTest() {
                    assertNotNull(cells, "Board build should initialize cells");
                    assertEquals(8, cells.length, "Board cells should be of length 8x8");
                    for (Object[] objects : cells) {
                        assertEquals(cells.length, objects.length, "Board cells should be of length 8x8");
                    }
                }

                @ParameterizedTest
                @MethodSource("getPieces")
                @DisplayName("Piece Type Test")
                void testPiece(int i, int j, String type, boolean isWhite) throws ReflectiveOperationException {
                    assertNotNull(cells);
                    Class<?> classObj = cells[i][j].getClass();
                    Object piece = classObj.getDeclaredField("piece").get(cells[i][j]);
                    String mss = "cell[%s][%s] should have %s %s".formatted(i, j, isWhite? "white":"black", type);
                    assertNotNull(piece, mss);
                    assertEquals(piece.getClass(), Class.forName(type), mss);
                    boolean isw = (boolean) Class.forName(MainTest.piece).getDeclaredField("isWhite").get(piece);
                    assertEquals(isWhite, isw, mss);
                    assertEquals(Class.forName(MainTest.piece).getDeclaredField("cell").get(piece), cells[i][j], "Piece.cell & Cell.piece should be symmetric");
                }

                static  Stream<Arguments> getPieces(){
                    ArrayList<Arguments> args = new ArrayList<>(40);
                    String[] pieces = {rook, knight, bishop, queen, king, bishop, knight, rook};
                    for (int i = 0; i < pieces.length; i++) {
                        args.add(arguments(0, i, pieces[i], false));
                    }
                    for (int i = 0; i < 8; i++) {
                       args.add(arguments(1, i, pawn, false));
                    }
                    for (int i = 0; i < 8; i++) {
                       args.add(arguments(6, i, pawn, true));
                    }
                    for (int i = 0; i < pieces.length; i++) {
                        args.add(arguments(7, i, pieces[i], true));
                    }
                    return args.stream();
                }

                @ParameterizedTest
                @MethodSource("getNullCells")
                @DisplayName("Piece Type Test")
                void testNullCell(int i, int j) throws ReflectiveOperationException {
                    Class<?> classObj = cells[i][j].getClass();
                    Object piece = classObj.getDeclaredField("piece").get(cells[i][j]);
                    String mss = "cell[%s][%s] should be null".formatted(i, j);
                    assertNull(piece, mss);
                }

                static  Stream<Arguments> getNullCells(){
                    ArrayList<Arguments> args = new ArrayList<>(40);
                    for (int i = 2; i <= 5; i++) {
                        for (int j = 0; j < 8; j++) {
                            args.add(arguments(i, j));
                        }
                    }
                    return args.stream();
                }
            }

        }

        @Nested
        @Order(1)
        class CellTest {


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(cell);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(point)), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Fields Exist")
            void testFieldsExist() throws ClassNotFoundException, NoSuchFieldException {
                Class<?> classObj = Class.forName(cell);

                assertDoesNotThrow(() -> classObj.getDeclaredField("location"), "Class has no field location");
                Field location = classObj.getDeclaredField("location");
                assertEquals(location.getType(), Class.forName(point), "Field location should be point");
                assertTrue(Modifier.isPublic(location.getModifiers()), "location should be public");

                assertDoesNotThrow(() -> classObj.getDeclaredField("piece"), "Class has no field piece");
                Field piece = classObj.getDeclaredField("piece");
                assertEquals(piece.getType(), Class.forName(MainTest.piece), "Field piece should be MainTest.piece");
                assertTrue(Modifier.isPublic(piece.getModifiers()), "piece should be public");
            }


        }

        @Nested
        @Order(2)
        class PieceTest {

            @Test
            @DisplayName("Test Abstract")
            void testAbstract() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(piece);
                assertTrue(Modifier.isAbstract(classObj.getModifiers()));
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(piece);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Fields Exist")
            void testFieldsExist() throws ClassNotFoundException, NoSuchFieldException {
                Class<?> classObj = Class.forName(piece);

                assertDoesNotThrow(() -> classObj.getDeclaredField("isWhite"), "Class has no field isWhite");
                Field isWhite = classObj.getDeclaredField("isWhite");
                assertEquals(isWhite.getType(), Boolean.TYPE, "Field isWhite should be Boolean.TYPE");
                assertTrue(Modifier.isPublic(isWhite.getModifiers()), "isWhite should be public");

                assertDoesNotThrow(() -> classObj.getDeclaredField("cell"), "Class has no field cell");
                Field cell = classObj.getDeclaredField("cell");
                assertEquals(cell.getType(), Class.forName(MainTest.cell), "Field cell should be MainTest.cell");
                assertTrue(Modifier.isPublic(cell.getModifiers()), "cell should be public");

                assertDoesNotThrow(() -> classObj.getDeclaredField("board"), "Class has no field board");
                Field board = classObj.getDeclaredField("board");
                assertEquals(board.getType(), Class.forName(MainTest.board), "Field board should be MainTest.board");
                assertTrue(Modifier.isPublic(board.getModifiers()), "board should be public");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(piece);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                assertTrue(Modifier.isAbstract(move1.getModifiers()), "move should be abstract");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(3)
        class PointTest {


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(point);
                assertDoesNotThrow(() -> classObj.getConstructor(Integer.TYPE , Integer.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Fields Exist")
            void testFieldsExist() throws ClassNotFoundException, NoSuchFieldException {
                Class<?> classObj = Class.forName(point);

                assertDoesNotThrow(() -> classObj.getDeclaredField("x"), "Class has no field x");
                Field x = classObj.getDeclaredField("x");
                assertEquals(x.getType(), Integer.TYPE, "Field x should be Integer.TYPE");
                assertTrue(Modifier.isPublic(x.getModifiers()), "x should be public");

                assertDoesNotThrow(() -> classObj.getDeclaredField("y"), "Class has no field y");
                Field y = classObj.getDeclaredField("y");
                assertEquals(y.getType(), Integer.TYPE, "Field y should be Integer.TYPE");
                assertTrue(Modifier.isPublic(y.getModifiers()), "y should be public");
            }


        }

        @Nested
        @Order(3)
        class RookTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(rook);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(rook);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(rook);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(4)
        class KnightTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(knight);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(knight);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(knight);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(5)
        class BishopTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(bishop);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(bishop);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(bishop);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(6)
        class QueenTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(queen);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(queen);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(queen);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(7)
        class KingTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(king);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(king);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(king);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }

        @Nested
        @Order(8)
        class PawnTest {

            @Test
            @DisplayName("Test Bases")
            void testBases() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(pawn);

                Class<?> base = Class.forName(piece);
                assertEquals(classObj.getSuperclass(), base, "class should extend piece");
            }


            @Test
            @DisplayName("Test Constructors Exist")
            void testConstructorsExist() throws ClassNotFoundException {
                Class<?> classObj = Class.forName(pawn);
                assertDoesNotThrow(() -> classObj.getConstructor(Class.forName(cell) , Class.forName(board) , Boolean.TYPE), "Constructor does not exist!");
            }

            @Test
            @DisplayName("Test Methods Exist")
            void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                Class<?> classObj = Class.forName(pawn);

                assertDoesNotThrow(() -> classObj.getMethod("move", Class.forName(cell)), "Class has no method move");
                Method move1 = classObj.getDeclaredMethod("move", Class.forName(cell));
                assertEquals(move1.getReturnType(), Void.TYPE, "move should return null");
                assertTrue(Modifier.isPublic(move1.getModifiers()), "move should be public");
                Stream<Class<?>> exs = Arrays.stream(move1.getExceptionTypes());
                Class<?> ex1 = Class.forName(invalidMovement);
                assertTrue(exs.anyMatch(e -> e.equals(ex1)), "move should throw invalidMovement");
            }


        }
    }


    @Nested
    @Order(6)
    class ExceptionsTest {
        @Test
        @DisplayName("Test InvalidMovementException Default Constructor Exist")
        void testInvalidMovementExceptionDefaultConstructorExist() throws ClassNotFoundException {
            Class<?> invMovClass = Class.forName(invalidMovement);
            assertDoesNotThrow(() -> invMovClass.getConstructor(), "InvalidMovementException has no default constructor");
        }
    }

    @Nested
    @Order(7)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class MoveTest{
        static Object[][] cells;

        @BeforeAll
        static void init() throws ReflectiveOperationException{
            Class<?> classObj = Class.forName(board);
            Object inst = classObj.getConstructor().newInstance();
            classObj.getMethod("build").invoke(inst);
            cells = (Object[][]) Class.forName(board).getField("cells").get(inst);
        }

        @ParameterizedTest
        @MethodSource("getValidMoves")
        @DisplayName("Test Valid Move")
        @Order(1)
        void testValidMove(int i1, int j1, int i2, int j2) throws ReflectiveOperationException{
            Class<?> classObj = cells[i1][j1].getClass();
            Object piece = classObj.getDeclaredField("piece").get(cells[i1][j1]);
            String mss = "cell[%s][%s] should have piece".formatted(i1, j1);
            assertNotNull(piece, mss);
            assertDoesNotThrow(
                    () -> Class.forName(MainTest.piece).getMethod("move", classObj).invoke(piece, cells[i2][j2]),
                    "move from %s|%s to %s|%s should be valid".formatted(i1, j1, i2, j2));
            mss = "move Method should update cell piece";
            assertEquals(piece, classObj.getDeclaredField("piece").get(cells[i2][j2]), mss);
            assertNull(classObj.getDeclaredField("piece").get(cells[i1][j1]), mss);
        }

        static Stream<Arguments> getValidMoves(){
            return Stream.of(
                    arguments(1, 0, 2, 0),
                    arguments(1, 7, 3, 7),
                    arguments(3, 7, 4, 7),
                    arguments(0, 1, 2, 2),
                    arguments(2, 2, 4, 3),
                    arguments(0, 7, 3, 7)
            );
        }

        @ParameterizedTest
        @MethodSource("getInvalidMoves")
        @DisplayName("Test Invalid Move")
        @Order(2)
        void testInvalidMove(int i1, int j1, int i2, int j2) throws ReflectiveOperationException{
            Class<?> classObj = cells[i1][j1].getClass();
            Object piece = classObj.getDeclaredField("piece").get(cells[i1][j1]);
            String mss = "cell[%s][%s] should have piece".formatted(i1, j1);
            assertNotNull(piece, mss);
            assertThrowsExactly(
                    Class.forName(invalidMovement).asSubclass(Throwable.class),
                    () -> Class.forName(MainTest.piece).getMethod("move", classObj).invoke(piece, cells[i2][j2]),
                    "move from %s|%s to %s|%s should be invalid".formatted(i1, j1, i2, j2));
            mss = "move Method shouldn't update cell piece";
            assertEquals(piece, classObj.getDeclaredField("piece").get(cells[i1][j1]), mss);
            assertNull(classObj.getDeclaredField("piece").get(cells[i2][j2]), mss);
        }

        static Stream<Arguments> getInvalidMoves(){
            return Stream.of(
                    arguments(2, 0, 4, 0),
                    arguments(4, 3, 5, 4),
                    arguments(3, 7, 4, 8)
            );
        }
    }
}