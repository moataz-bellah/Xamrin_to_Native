package patterns;

public class JavaRealmHandler {
    public String RealmInitStatementPattern(String statement,String identifier){
        //String identifier = statement.substring(3,statement.indexOf("="));
        String javaOutput = "Realm " + identifier + " = Realm.getInstance(new RealmConfiguration.Builder().name(realmName).build());\n";

        return javaOutput;
    }
}
