package org.example;

public class ModifierInfo extends  Testable{

    public  String name;

    public ModifierInfo(String name) {
        this.name = name;
    }

    public static ModifierInfo[] of(String ...names){
        ModifierInfo[] arr = new ModifierInfo[names.length];
        for (int i = 0; i < names.length; i++) {
            arr[i] = new ModifierInfo(names[i]);
        }
        return  arr;
    }

    @Override
    public String createTest(Object... obs) {
        return """

                \t\t\tassertTrue(Modifier.is%s(%s.getModifiers()), "%s should be %s");""".formatted(capitalize(name), obs[1], obs[2], name);
    }
}
