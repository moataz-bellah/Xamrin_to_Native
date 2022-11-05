package patterns;

import java.util.ArrayList;

public class PatternsMatcher {
    public boolean CheckRealmInitStatementPattern(String statement){
        if(statement.matches("var[a-zA-Z]+=Realm\\.GetInstance\\(\\)"))
            return true;
        return false;
    }
    public ArrayList<String> RealmInitStatementPattern(String statement){
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        ArrayList<String> output = new ArrayList<>();
        if(CheckRealmInitStatementPattern(statement)){
            String identifier = statement.substring(3,statement.indexOf("="));
            String javaOutput = javaRealmHandler.RealmInitStatementPattern(statement,identifier);
            String swiftOutput = swiftRealmHandler.RealmInitStatementPattern(statement,identifier);
            output.add(javaOutput);
            output.add(swiftOutput);
            return output;
        }

        return output;
    }
    public boolean CheckGettersAndSettersStatementPattern(String statement){
        if(statement.matches("[a-zA-Z]+\\([a-zA-Z]+\\)\\{get;set;\\}"))
            return true;
        return false;
    }
    public ArrayList<String> GettersAndSettersStatementPattern(String statement,String methodName,ArrayList<String> javaParameters,
                                                               ArrayList<String>swiftParameters,String returnType){
        ArrayList<String> output = new ArrayList<>();
        String javaOutput = "";
        String swiftOuput = "";
        String javaParameter = javaParameters.get(0);
        String swiftParameter = swiftParameters.get(0);
        String javaParameterName = "";
        String swiftParameterName = "";
        javaParameterName = javaParameters.get(0).substring(javaParameter.indexOf(" "));
        swiftParameterName = swiftParameter.substring(swiftParameter.indexOf(" "),swiftParameter.indexOf(":"));
        if(CheckGettersAndSettersStatementPattern(statement)){
                javaOutput = "public " + "void " + methodName + "(" + javaParameters.get(0) + ") {\n this." + methodName
                        + " = " + javaParameterName + "; \n}\n";
                javaOutput+=("public " + returnType + " " + methodName + "(){\n return this." + methodName + ";\n}\n");

                swiftOuput = "public " + methodName + "(" + swiftParameters.get(0) + ")->Void{\n self."
                        + methodName + " = " + swiftParameterName + "\n}\n";
                swiftOuput+=("public " + methodName + "()->" + returnType + "{\n return self."+methodName+ "\n}\n");

                output.add(javaOutput);
                output.add(swiftOuput);
                return output;

        }
        return output;

    }
}
