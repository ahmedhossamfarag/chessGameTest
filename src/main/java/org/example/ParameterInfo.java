package org.example;

public class ParameterInfo  extends Testable{
    public String type;

    public ParameterInfo(String type) {
        this.type = type;
    }

    public static ParameterInfo[] of(String ...types){
        ParameterInfo[] arr = new ParameterInfo[types.length];
        for (int i = 0; i < types.length; i++) {
            arr[i] = new ParameterInfo(types[i]);
        }
        return  arr;
    }

    @Override
    public String createTest(Object... obs) {
        return getClassOf(type);
    }
}
