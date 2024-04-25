package org.example;

public class MethodInfo  extends Testable{

    public String name;

    public String returnType;

    public ModifierInfo[] modifiers;

    public ParameterInfo[] parameters;

    public  ExceptionInfo[] exceptions;

    public MethodInfo(String name, String returnType, ModifierInfo[] modifiers, ParameterInfo[] parameters, ExceptionInfo[] exceptions) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.exceptions = exceptions;
    }


    public static MethodInfo[] of(MethodInfo ...methods){
        return  methods;
    }

    @Override
    public String createTest(Object... obs) {
        String tName = name + obs[0];
        String prm = createParamsTest();
        return """


                            \t\t\tassertDoesNotThrow(() -> classObj.getMethod("%s"%s), "Class has no method %s");
                            \t\t\tMethod %s = classObj.getDeclaredMethod("%s"%s);
                            \t\t\tassertEquals(%s.getReturnType(), %s, "%s should return %s");%s%s"""
                .formatted(name, prm, name, tName, name, prm, tName, getClassOf(returnType), name, returnType,
                    createTest(modifiers, "", tName, name), createExceptionsTest(tName));
    }

    private String createExceptionsTest(String tName) {
        if (exceptions == null || exceptions.length == 0)
            return  "";
        return """
                
                \t\t\tStream<Class<?>> exs = Arrays.stream(%s.getExceptionTypes());%s""".formatted(tName, createTest(exceptions, "", name));
    }

    private String createParamsTest() {
        if(parameters == null || parameters.length == 0)
            return "";
        return ", " + createTest(parameters, " , ");
    }


}
