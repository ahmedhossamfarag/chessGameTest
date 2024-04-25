package org.example;

public class FieldInfo  extends Testable{
    public String name;

    public String type;

    public ModifierInfo[] modifiers;

    public FieldInfo(String name, String type, ModifierInfo[] modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public static FieldInfo[] of(FieldInfo ...fields){
        return  fields;
    }

    @Override
    public String createTest(Object... obs) {
        return """
                         
                         
                         \t\t\tassertDoesNotThrow(() -> classObj.getDeclaredField("%s"), "Class has no field %s");
                         \t\t\tField %s = classObj.getDeclaredField("%s");
                         \t\t\tassertEquals(%s.getType(), %s, "Field %s should be %s");%s""".formatted(
                                 name, name, name, name, name, getClassOf(type), name, type, createTest(modifiers, "", name, name
                )
        );
    }
}
