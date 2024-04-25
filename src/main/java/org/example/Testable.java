package org.example;


public abstract class Testable {
    public abstract String createTest(Object... obs);



    public static String capitalize(String s) {
        if(s == null || s.isBlank())
            return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }


    public  static String createTest(Testable[] testable, String join, Object... params){
        if(testable == null || testable.length == 0)
            return "";
        Object[] pass = new Object[params.length + 1];
        System.arraycopy(params, 0, pass, 1, params.length);
        String[] cons = new String[testable.length];
        for (int i = 0; i < testable.length; i++) {
            pass[0] = i+1;
            cons[i] = testable[i].createTest(pass);
        }
        return String.join(join, cons);
    }

    public static String getClassOf(String type){
        if(type == null)
            return "Void.TYPE";
        if(type.endsWith(".TYPE"))
            return  type;
        return "Class.forName(" + type + ")";
    }
}
