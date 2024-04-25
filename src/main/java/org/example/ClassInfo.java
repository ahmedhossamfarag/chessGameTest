package org.example;

import java.util.Arrays;

public class ClassInfo extends Testable{
    public  String name;

    public  boolean isAbstract;

    public BaseInfo base;

    public  BaseInfo[] interfaces;

    public  ConstructorInfo[] constructors;

    public FieldInfo[] fields;

    public  MethodInfo[] methods;

    public ClassInfo(String name, boolean isAbstract, BaseInfo base, BaseInfo[] interfaces, ConstructorInfo[] constructors, FieldInfo[] fields, MethodInfo[] methods) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.base = base;
        this.interfaces = interfaces;
        this.constructors = constructors;
        this.fields = fields;
        this.methods = methods;
    }

    public String createTest(Object... obs){
        return """
                    @Nested
                    @Order(%s)
                    class %sTest {
                        %s%s
                        
                        @Test
                        @DisplayName("Test Constructors Exist")
                        void testConstructorsExist() throws ClassNotFoundException {
                            Class<?> classObj = Class.forName(%s);%s
                        }
                        %s%s
                        
                    }
                """.formatted(obs[0], capitalize(name), createAbsTest(), createBasesTest(), name, createConstructorsTest(), createFieldsTest(), createMethodsTest());
    }

    private Object createAbsTest() {
        if (!isAbstract)
            return "";
        return """       

                        @Test
                        @DisplayName("Test Abstract")
                        void testAbstract() throws ClassNotFoundException {
                            Class<?> classObj = Class.forName(%s);
                            assertTrue(Modifier.isAbstract(classObj.getModifiers()));
                        }
                """.formatted(name);
    }

    private Object createBasesTest() {
        if( base == null && (interfaces == null || interfaces.length == 0))
            return "";
        String baseTest = base == null? "" : base.createTest();
        return """

                                @Test
                                @DisplayName("Test Bases")
                                void testBases() throws ClassNotFoundException {
                                    Class<?> classObj = Class.forName(%s);%s%s
                                }
                        """.formatted(name, baseTest, createTest(interfaces, ""));
    }

    private String createConstructorsTest() {
        if(constructors == null)
            return new ConstructorInfo(ParameterInfo.of()).createTest();
        return createTest(constructors, "");
    }

    private String createFieldsTest() {
        if (fields == null)
            return "";
        return """
                     
                        @Test
                        @DisplayName("Test Fields Exist")
                        void testFieldsExist() throws ClassNotFoundException, NoSuchFieldException {
                            Class<?> classObj = Class.forName(%s);%s
                        }
                """.formatted(name, createTest(fields, ""));
    }
    private String createMethodsTest() {
        if(methods == null)
            return "";
        return """ 
                       
                        @Test
                        @DisplayName("Test Methods Exist")
                        void testMethodsExist() throws ClassNotFoundException, NoSuchMethodException {
                            Class<?> classObj = Class.forName(%s);%s
                        }
                """.formatted(name, createTest(methods, ""));
    }

}
