package patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JavaRealmHandler {
    public Map<String,String> javaRealmOperators;
    JavaRealmHandler(){
        javaRealmOperators = new HashMap<>();
        javaRealmOperators.put("<","lessThan");
        javaRealmOperators.put("<=","lessThanOrEqualTo");
        javaRealmOperators.put(">","greaterThan");
        javaRealmOperators.put(">=","greaterThanOrEqualTo");
        javaRealmOperators.put("==","equalTo");
    }

    public String RealmInitStatementPattern(String statement,String identifier){
        //String identifier = statement.substring(3,statement.indexOf("="));
        String javaOutput = "Realm " + identifier + " = Realm.getInstance(new RealmConfiguration.Builder().name(\"realmName\").build());\n";

        return javaOutput;
    }


    // RealmResults<Task> Tasks = backgroundThreadRealm.where(Task.class).findAll();
    public String RealmRetrieveStatementPattern(String identifier,String className,String objectName){

        String javaOutput = "RealmResults<" + className+">" + identifier +
                " = "  + objectName + ".where(" + className + ".class).findAll();\n";
        return javaOutput;
    }


    public String RealmWhereLessThanStatementPattern(String identifier,String className
            ,String objectName,String fieldName,String value){
        //RealmQuery<Task> openTasks = Tasks.where().lessThan("age",20);
        String javaOutput = "";
        javaOutput = RealmRetrieveStatementPattern(identifier+"0",className,objectName);

        javaOutput+=("RealmQuery<" + className + ">" + identifier + " = "+ identifier+"0.where().lessThan(\"" + fieldName + "\"," + value + ");\n");
        return javaOutput;
    }



    public String RealmWhereStatementPattern(String identifier, String className
            , String objectName, ArrayList<String> fieldNames, ArrayList<String> operators,ArrayList<String> values){
        //RealmQuery<Task> openTasks = Tasks.where().lessThan("age",20);
        String javaOutput = "";
        javaOutput = RealmRetrieveStatementPattern(identifier+"0",className,objectName);

        javaOutput+=("RealmQuery<" + className + ">" + identifier + " = "+ identifier+"0.where().");

        for(int i = 0; i< fieldNames.size();i++){
                javaOutput+=(javaRealmOperators.get(operators.get(i)) + "(\"" +
                        fieldNames.get(i) + "\"," + values.get(i) + ").");
        }
        return javaOutput + ";\n";
    }

    public String RealmOrderByStatementPattern(String identifier,String className
            ,String objectName,String fieldName){
        //        RealmResults<Task> openTasks2 = Tasks.sort("age");
        String javaOutput = "";
        javaOutput = RealmRetrieveStatementPattern(identifier+"0",className,objectName);

        javaOutput+=("RealmResults<" + className + ">" + identifier + " = "+ identifier+"0.sort(\"" + fieldName + "\"" + ");\n");
        return javaOutput;
    }


    /*

    Task Task = new Task("New Task");
backgroundThreadRealm.executeTransaction (transactionRealm -> {
    transactionRealm.insert(Task);
});
     */
    public String RealmWriteStatementPattern(String objectName,String className,
                                             ArrayList<String> fields,ArrayList<String> values){

        String javaOutput = className + " Task = new " + className + "();\n";
        for(int i = 0;i<fields.size();i++){
            javaOutput+=("Task." + fields.get(i) + "(" + values.get(i) + ");\n");
        }

        javaOutput+=(objectName + ".executeTransaction (transactionRealm -> {\n transactionRealm.insert(Task);});\n");


        return javaOutput;
    }

    public String RealmSettersAndGettersStatementPattern(String decorator,String methodName,String returnType){
        String javaOutput = "";
        if(decorator.equals(""))
            javaOutput = "@" + decorator;
        javaOutput+=" private " + returnType +" " +  methodName+ ";\n";

        javaOutput+="public void " + methodName + "(" + returnType + " x){\nthis." +methodName + " = x;}\n";
        javaOutput+="public " + returnType + " " + methodName+ "(){\n return this." + methodName + ";}\n";
        return javaOutput;
    }

    public String RealmWriteForDeleteStatementPattern(String objectName,String queryName){
        String javaOutput = objectName + ".executeTransaction(bla->{\n" + queryName + ".findFirst().deleteFromRealm();\n });\n";

        return javaOutput;
    }

    public String RealmWriteForUpdateStatementPattern(String realmObjectName,String objectName,
                                                      ArrayList<String>fields,ArrayList<String>values){
        String javaOutput = realmObjectName + ".executeTransaction(bla->{\n ";
        for(int i = 0; i< fields.size();i++){
            javaOutput+=objectName + "." + fields.get(i) + "(" + values.get(i) + ");\n";
        }
        javaOutput+="});\n";
        return javaOutput;
    }
    // new App(new AppConfiguration.Builder(myRealmAppId).build())
    public String RealmInitializeAppStatementPattern(String identifier,String value){
        String javaOutput = "App " + identifier + " = new App(new AppConfiguration.Builder(" + value + ").build());\n";
        javaOutput+="Credentials anonymousCredentials = Credentials.anonymous();\n";
        return javaOutput;
    }


    public String RealmLoginAnonymousStatementPattern(String identifier,String objectName){
        String javaOutput = "AtomicReference<User>" + identifier + " = AtomicReference<User>();\n";
        javaOutput+=objectName + ". loginAsync(anonymousCredentials, it -> {\n if (it.isSuccess()) {\n";
        javaOutput+=identifier + ".set(" + objectName + ".currentUser());\n } else {\n" +
                "        Log.e(\"AUTH\", it.getError().toString());\n" +
                "    }\n});\n";

        return javaOutput;
    }


    public String RealmLogoutStatementPattern(String identifier){
        String javaOutput = identifier + "user.get().logOutAsync( result -> {\n" +
                "    if (result.isSuccess()) {\n" +
                "        Log.v(\"AUTH\", \"Successfully logged out.\");\n" +
                "    } else {\n" +
                "        Log.e(\"AUTH\", result.getError().toString());\n" +
                "    }\n" +
                "});\n";

        return javaOutput;
    }
}