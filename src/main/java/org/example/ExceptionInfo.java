package org.example;

public class ExceptionInfo extends Testable{

    public  String name;

    public ExceptionInfo(String name) {
        this.name = name;
    }

    public static ExceptionInfo[] of(String ...names){
        ExceptionInfo[] arr = new ExceptionInfo[names.length];
        for (int i = 0; i < names.length; i++) {
            arr[i] = new ExceptionInfo(names[i]);
        }
        return  arr;
    }

    @Override
    public String createTest(Object... obs) {
        return """

                    \t\t\tClass<?> ex%s = Class.forName(%s);
                    \t\t\tassertTrue(exs.anyMatch(e -> e.equals(ex%s)), "%s should throw %s");""".formatted(obs[0], name, obs[0], obs[1], name);
    }
}
