package org.example;


public class Main {
    static  String str = "board, cell, piece, rook, knight, bishop, queen, king, pawn, point";

    static  String piece = "rook, knight, bishop, queen, king, pawn";

    public static void main(String[] args) throws Exception {

        String[] models = str.split(",");
        for(String s : models){
            System.out.printf("static String %s = modelsPackage + '.' + \"%s\";%n", s.trim(), capitalize(s.trim()));
        }

        ClassInfo[] classes = {
                new ClassInfo("board", false, null, null, null,
                        FieldInfo.of(new FieldInfo("cells", "\"[[L\"+cell+';'",  ModifierInfo.of("public"))),
                        MethodInfo.of(new MethodInfo("build", null,  ModifierInfo.of("public"), null, null))
                ),
                new ClassInfo("cell", false, null, null,
                        ConstructorInfo.of(ParameterInfo.of("point")),
                        FieldInfo.of(
                                new FieldInfo("location", "point", ModifierInfo.of("public")),
                                new FieldInfo("piece", "MainTest.piece",  ModifierInfo.of("public"))),
                        null
                ),
                new ClassInfo("point", false, null, null,
                        ConstructorInfo.of(ParameterInfo.of("Integer.TYPE", "Integer.TYPE")),
                        FieldInfo.of(
                                new FieldInfo("x", "Integer.TYPE", ModifierInfo.of("public")),
                                new FieldInfo("y", "Integer.TYPE", ModifierInfo.of("public"))),
                        null
                ),
                new ClassInfo("piece", true, null, null,
                        ConstructorInfo.of(ParameterInfo.of("cell", "board", "Boolean.TYPE")),
                        FieldInfo.of(
                                new FieldInfo("isWhite", "Boolean.TYPE",  ModifierInfo.of("public")),
                                new FieldInfo("cell", "MainTest.cell",  ModifierInfo.of("public")),
                                new FieldInfo("board", "MainTest.board",  ModifierInfo.of("public"))),
                        MethodInfo.of(new MethodInfo("move", null,  ModifierInfo.of("public", "abstract"), ParameterInfo.of("cell"), ExceptionInfo.of("invalidMovement")))
                ),
        };

        for (int i = 0; i < classes.length; i++) {
            System.out.println(classes[i].createTest(i));
        }

        String[] pieces = piece.split(",");

        for (int i = 0; i < pieces.length; i++) {
            ClassInfo c = new ClassInfo(pieces[i].trim(), false, new BaseInfo("piece"), null,
                    ConstructorInfo.of(ParameterInfo.of("cell", "board", "Boolean.TYPE")),
                    null,
                    MethodInfo.of(new MethodInfo("move", null,  ModifierInfo.of("public"), ParameterInfo.of("cell"), ExceptionInfo.of("invalidMovement")))
            );
            System.out.println(c.createTest(i+3));
        }

        for (int i = 0; i < pieces.length; i++) {
            System.out.printf("args.add(Arguments.arguments(0, %d, %s, false));%n", i, pieces[i].trim());
        }
        for (int i = 0; i < pieces.length; i++) {
            System.out.printf("args.add(Arguments.arguments(0, %d, %s, true));%n", i, pieces[i].trim());
        }

    }

    private static String capitalize(String s) {
        if(s == null || s.isBlank())
            return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}