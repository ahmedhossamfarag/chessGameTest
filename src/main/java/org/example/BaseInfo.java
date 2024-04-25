package org.example;

public class BaseInfo extends Testable{

    public  String name;

    public  boolean isClass;

    public BaseInfo(String name){
        this(name, true);
    }

    public BaseInfo(String name, boolean isClass) {
        this.name = name;
        this.isClass = isClass;
    }

    public static BaseInfo[] of(String  ...bases){
        BaseInfo[] arr = new BaseInfo[bases.length];
        for (int i = 0; i < bases.length; i++) {
            arr[i] = new BaseInfo(bases[i], false);
        }
        return arr;
    }

    @Override
    public String createTest(Object... obs) {
        if(isClass)
            return """
                        
                        
                \t\t\tClass<?> base = Class.forName(%s);
                \t\t\tassertEquals(classObj.getSuperclass(), base, "class should extend %s");""".formatted(name, name);
        return """ 
                        
                        
                \t\t\tClass<?> inter%s = Class.forName(%s);
                \t\t\tassertDoesNotThrow(() -> classObj.asSubclass(Class.forName(inter%s)), "class should implement %s");""".formatted(obs[0], name, obs[0], name);
    }
}
