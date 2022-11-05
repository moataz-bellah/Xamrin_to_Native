package patterns;

public class SwiftRealmHandler {
    public String RealmInitStatementPattern(String statement,String identifier){
        String swiftOutput = "";
        swiftOutput = "let " + identifier + " = try! Realm()\n";
        return swiftOutput;
    }
}
