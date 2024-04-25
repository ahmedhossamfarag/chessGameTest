package org.example;

public class ConstructorInfo  extends Testable{

    public ParameterInfo[] params;

    public ConstructorInfo(ParameterInfo[] params) {
        this.params = params;
    }

    public static ConstructorInfo[] of(ParameterInfo[] ...paramsList){
        if(paramsList == null || paramsList.length == 0)
            return null;
        ConstructorInfo[] cons = new ConstructorInfo[paramsList.length];
        for (int i = 0; i < paramsList.length; i++) {
            cons[i] = new ConstructorInfo(paramsList[i]);
        }
        return cons;
    }
    @Override
    public String createTest(Object... obs) {
        String prm = createTest(params, " , ");
        return """
               
               \t\t\tassertDoesNotThrow(() -> classObj.getConstructor(%s), "Constructor does not exist!");""".formatted(prm);
    }
}
