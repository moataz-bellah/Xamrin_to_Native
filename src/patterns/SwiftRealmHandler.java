package patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SwiftRealmHandler {

    public Map<String,String> swiftRealmOperators;
    SwiftRealmHandler(){
        swiftRealmOperators = new HashMap<>();
        swiftRealmOperators.put("<","lessThan");
        swiftRealmOperators.put("<=","lessThanOrEqualTo");
        swiftRealmOperators.put(">","greaterThan");
        swiftRealmOperators.put(">=","greaterThanOrEqualTo");
        swiftRealmOperators.put("==","equalTo");
    }
    public String RealmInitStatementPattern(String statement,String identifier){
        String swiftOutput = "";
        swiftOutput = "let " + identifier + " = try! Realm()\n";
        return swiftOutput;
    }



    public String RealmRetrieveStatementPattern(String identifier,String className,String objectName){

        String swiftOutput = "let " + identifier + "=" + objectName +
                ".objects("  + className + ".self)\n";
        return swiftOutput;
    }

    public String RealmWhereLessThanStatementPattern(String identifier,String className
            ,String objectName,String fieldName,String value){
        //let todosInProgress = todos.where {
        //    $0.status == "InProgress"
        //}
        String swiftOutput = "";
        swiftOutput = RealmRetrieveStatementPattern(identifier+"0",className,objectName);

        swiftOutput+=("let " + identifier  + " = "+ identifier+"0.where() { \n $0." + fieldName + "<" + value + "}\n");
        return swiftOutput;
    }

    public String RealmWhereStatementPattern(String identifier, String className
            , String objectName, ArrayList<String> fieldNames,ArrayList<String> operators, ArrayList<String> values){
        //let todosInProgress = todos.where {
        //    $0.status == "InProgress"
        //}
        String swiftOutput = "";
        swiftOutput = RealmRetrieveStatementPattern(identifier+"0",className,objectName);

        swiftOutput+=("let " + identifier  + " = "+ identifier+"0.where { \n)");
        for(int i = 0; i< fieldNames.size();i++){
            swiftOutput+=("$0." + fieldNames.get(i) + operators.get(i) + values.get(i) + ",");
        }
        swiftOutput+="}\n";
        return swiftOutput;
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
    let todo2 = Todo2(name: "Do laundry", ownerId: user.id)
try! realm.write {
    realm.add(todo2)
}
     */
    public String RealmWriteStatementPattern(String objectName,String className,
                                             ArrayList<String> fields,ArrayList<String> values){

        String swiftOutput = "let Task = " + className + "();\n";
        for(int i = 0;i<fields.size();i++){
            swiftOutput+=("Task." + fields.get(i) + "(" + values.get(i) + ")\n");
        }

        swiftOutput+=("try! "+objectName + ".write {\n " + objectName+ ".add(Task)}\n");


        return swiftOutput;
    }
    /*
    @Persisted(primaryKey: true) var _id: ObjectId
   @Persisted var name: String = ""
   @Persisted var status: String = ""
   @Persisted var ownerId: String

     */

    public String RealmSettersAndGettersStatementPattern(String decorator,String methodName,String returnType){
        String swiftOutput = "@Persisted";

        if(decorator.equals("PrimaryKey"))
            swiftOutput+="(primaryKey: true)";
        swiftOutput+=" var " + methodName + ": " + returnType + "\n";
        swiftOutput+="func " + methodName + "(x:" + returnType + ") -> Void{\n self." + methodName + "=x}\n";
        swiftOutput+="func " + methodName + "()->" + returnType + "{\n return self." + methodName + "}\n";
        return swiftOutput;
    }
/*
try! realm.write {
    // Delete the Todo.
    realm.delete(todoToDelete)
}
 */
    public String RealmWriteForDeleteStatementPattern(String objectName,String queryName){
        String swiftOutput = "try! " + objectName + ".write {\n " + objectName + ".delete(" + queryName + ")\n}\n";
        return swiftOutput;
    }
    /*
    try! realm.write {
    todoToUpdate.status = "InProgress"
}
     */
    public String RealmWriteForUpdateStatementPattern(String realmObjectName,String objectName,
                                                      ArrayList<String> fields, ArrayList<String> values){
        String swiftOutput = "try! " + realmObjectName + ".write {\n ";
        for(int i = 0 ; i<fields.size();i++){
            swiftOutput+=objectName + "." + fields.get(i) + "(" + values.get(i) + ")\n";
        }
        return swiftOutput + "}\n";
    }
    // let app = App(id: APP_ID)
    public String RealmInitializeAppStatementPattern(String identifier,String value){
        String swiftOutput = "let " + identifier + " = App(id:" + value + ")\n";
        return swiftOutput;
    }
    /*
    do {
    let user = try await app.login(credentials: Credentials.anonymous)
    print("Successfully logged in user: \(user)")
    await openSyncedRealm(user: user)
} catch {
    print("Error logging in: \(error.localizedDescription)")
}
     */
    public String RealmLoginAnonymousStatementPattern(String identifier,String objectName){
        String swiftOutput = "do{\n let " + identifier + " = try await " +objectName + ".login(credentials: Credentials.anonymous)\n";
        swiftOutput+="await openSyncedRealm(user:" + identifier + ")\n}\n";
        swiftOutput+="catch {\n" +
                "    print(\"Error logging in: \\(error.localizedDescription)\")\n" +
                "}\n";
        return swiftOutput;
    }
    // app.currentUser?.logOut { (error) in
    //    // user is logged out or there was an error
    //}
    public String RealmLogoutStatementPattern(String identifier){
        String swiftOutput = identifier + ".currentUser?.logOut { (error) in\n print(\"Error\")\n}\n";

        return swiftOutput;
    }
}
