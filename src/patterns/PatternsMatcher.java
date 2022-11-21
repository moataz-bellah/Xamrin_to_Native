package patterns;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternsMatcher {
    public boolean firstTime  = true;
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
    public boolean CheckRealmRetrieveAllStatementPattern(String statement){
        if(statement.matches("var[a-zA-Z]+=[a-zA-Z]+\\.All<[a-zA-Z]+>\\(\\)"))
            return true;
        return false;
    }

    public ArrayList<String> RealmRetrieveAllStatementPattern(String statement){
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        ArrayList<String> output = new ArrayList<>();
        if(CheckRealmRetrieveAllStatementPattern(statement)){
            String identifier = statement.substring(3,statement.indexOf("="));
            String className = statement.substring(statement.indexOf("<")+1,statement.indexOf(">"));
            String objectName = statement.substring(statement.indexOf("=")+1,statement.indexOf("."));

            String javaOutput = javaRealmHandler.RealmRetrieveStatementPattern(identifier,className,objectName);
            String swiftOutput = swiftRealmHandler.RealmRetrieveStatementPattern(identifier,className,objectName);
            output.add(javaOutput);
            output.add(swiftOutput);

            return output;
        }

        return output;
    }

    public boolean CheckRealmWhereLessThanStatementPattern(String statement){
                                                                               // var lessExpensiveGuitars = realm.All<Guitar>().Where(g => g.Price < 400);
        if(statement.matches("var[a-zA-Z]+=[a-zA-Z]+\\.All<[a-zA-Z]+>\\(\\)\\.Where\\([a-zA-Z]+=>[a-zA-Z]+\\.[a-zA-Z]+(>=|<=|==|<|>)(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?)(&&[a-zA-Z]+\\.[a-zA-Z]+(>=|<=|==|<|>)(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?))*\\)"))
            return true;
        return false;
    }
    // var lessExpensiveGuitars = realm.All<Guitar>().Where(g => g.Price < 400);
    public ArrayList<String> RealmWhereLessThanStatementPattern(String statement){
        String patt = "\\([a-zA-Z]+=>[a-zA-Z]+\\.[a-zA-Z]+(>=|<=|==|<|>)(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?)(&&[a-zA-Z]+\\.[a-zA-Z]+(>=|<=|==|<|>)(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?))*\\)";

        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(statement);
        int start,end = -1;
        String subString = "";
        if(matcher.find()){
            start = matcher.start();
            end = matcher.end();
            subString = matcher.group();

        }

        String[] conditions = subString.split("&&");

        conditions[0] = conditions[0].substring(4);
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for(int i = 0;i<conditions.length;i++){
            String operatorRegx = "(>=|<=|==|<|>)";
            Pattern pattern2 = Pattern.compile(operatorRegx);
            Matcher matcher2 = pattern2.matcher(conditions[i]);
            if(matcher2.find()){
             //   System.out.println(" OPERATOR " + matcher2.group());
                operators.add(matcher2.group());
           //     System.out.println(" ID --> " + conditions[i].substring(conditions[i].indexOf(".")+1,matcher2.start()));
                fields.add(conditions[i].substring(conditions[i].indexOf(".")+1,matcher2.start()));
         //       System.out.println(" VALUE ---> " + conditions[i].substring(matcher2.end()));
                values.add(conditions[i].substring(matcher2.end()));
            }

        }




        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();

        ArrayList<String> output = new ArrayList<>();

        if(CheckRealmWhereLessThanStatementPattern(statement)){
            String identifier = statement.substring(3,statement.indexOf("="));
            String className = statement.substring(statement.indexOf("<")+1,statement.indexOf(">"));
            String objectName = statement.substring(statement.indexOf("=")+1,statement.indexOf("."));
//            String fieldNameChunk1 = statement.substring(statement.indexOf("=>")+1,statement.lastIndexOf("<"));
  //          String fieldName = fieldNameChunk1.substring(fieldNameChunk1.indexOf(".")+1);
    //        String value = statement.substring(statement.lastIndexOf("<")+1,statement.lastIndexOf(")"));
            String javaOutput = javaRealmHandler.RealmWhereStatementPattern(identifier,className,objectName,fields,operators,values);
            String swiftOutput = swiftRealmHandler.RealmWhereStatementPattern(identifier,className,objectName,fields,operators,values);

      //      String javaOutput = javaRealmHandler.RealmWhereLessThanStatementPattern(identifier,className,objectName,fieldName,value);
        //    String swiftOutput = swiftRealmHandler.RealmWhereLessThanStatementPattern(identifier,className,objectName,fieldName,value);

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
    public boolean CheckGettersAndSettersStatementPattern2(String statement){
        if(statement.matches("(\\[PrimaryKey\\]|\\[Required\\])?public(ObjectId|string|double|int)[a-zA-Z]+\\{get;set;\\}"))
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

    public ArrayList<String> GettersAndSettersStatementPattern2(String statement){
        ArrayList<String> output = new ArrayList<>();
        String javaOutput = "";
        String swiftOuput = "";
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        if(CheckGettersAndSettersStatementPattern2(statement)){
            String decorator = "";
                if(statement.contains("[PrimaryKey]")||statement.contains("[Required]"))
                    decorator = statement.substring(statement.indexOf("[")+1,statement.indexOf("]"));

            String returnTypeRegex = "(ObjectId|string|double|int)";
            int start = -1;
            int end = -1;
            String subString = "";
            String methodName = "";
            Pattern pattern = Pattern.compile(returnTypeRegex);
            Matcher matcher = pattern.matcher(statement);
            if(matcher.find()){
                start = matcher.start();
                end = matcher.end();
                subString = matcher.group();

            }
            if((subString != "") && (start != -1) && (end != -1) ){

                methodName = statement.substring(end,statement.indexOf("{"));


            }
            javaOutput =  javaRealmHandler.RealmSettersAndGettersStatementPattern(decorator,methodName,subString);
            swiftOuput = swiftRealmHandler.RealmSettersAndGettersStatementPattern(decorator,methodName,subString);


            output.add(javaOutput);
            output.add(swiftOuput);
            return output;

        }
        return output;

    }

    public boolean RealmWriteStatementPatternCheck(String statement){
                                                        // realm.Write(()=>{realm.Add(newGuitar(){Make="Gibson"});})
            if(statement.matches("[a-zA-Z]+\\.Write\\(\\(\\)=>\\{[a-zA-Z]+\\.Add\\(new[a-zA-Z]+\\(\\)\\{([a-zA-Z]+=(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?),?)+\\}\\);\\}\\)"))
                return true;
        return false;
    }
    public ArrayList<String> RealmWriteStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        String pattern = "[a-zA-Z]+\\.Add\\(new[a-zA-Z]+\\(\\)\\{([a-zA-Z]+=(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?),?)+\\}\\);";
        Pattern ptrn = Pattern.compile(pattern);
        Matcher matcher = ptrn.matcher(statement);
        String className = "";
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        String objectName = statement.substring(0,statement.indexOf("."));

        int start = -1;
        int end = -1;
        String subString = "";
        if(matcher.find()){
            start = matcher.start();
            end = matcher.end();
            subString = matcher.group();


        }

        if(!(subString.isEmpty())){
             className = subString.substring(subString.indexOf("new")+3,subString.indexOf(")")-1);
             String subString2 = subString.substring(subString.indexOf("{")+1,subString.lastIndexOf("}"));
             String assignments[] = subString2.split(",");
             for(int i=0;i<assignments.length;i++){

                 fields.add(assignments[i].substring(0,assignments[i].indexOf("=")));
                 values.add(assignments[i].substring(assignments[i].indexOf("=")+1));

             }
            String javaOutput = javaRealmHandler.RealmWriteStatementPattern(objectName,className,fields,values);
            String swiftOutput = swiftRealmHandler.RealmWriteStatementPattern(objectName,className,fields,values);

            output.add(javaOutput);
            output.add(swiftOutput);
        }

        return output;
    }




    public boolean CheckRealmOrderByStatementPattern(String statement){
        if(statement.matches("(var[a-zA-Z]+=)?[a-zA-Z]+\\.All<[a-zA-Z]+>\\(\\)\\.OrderBy\\([a-zA-Z]+=>[a-zA-Z]+\\.[a-zA-Z]+\\)")){
            return true;
        }
        return false;
    }

    public ArrayList<String> RealmOrderByStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        if(CheckRealmOrderByStatementPattern(statement)) {
            String identifier = statement.substring(3, statement.indexOf("="));
            String className = statement.substring(statement.indexOf("<") + 1, statement.indexOf(">"));
            String objectName = statement.substring(statement.indexOf("=") + 1, statement.indexOf("."));
            String fieldNameChunk1 = statement.substring(statement.indexOf("=>") + 1);
            String fieldName = fieldNameChunk1.substring(fieldNameChunk1.indexOf(".") + 1);

            String javaOutput = javaRealmHandler.RealmOrderByStatementPattern(identifier, className, objectName, fieldName);
            String swiftOutput = swiftRealmHandler.RealmOrderByStatementPattern(identifier, className, objectName, fieldName);

            output.add(javaOutput);
            output.add(swiftOutput);
        }
     return output;
    }

    public boolean CheckRealmWriteForDeleteStatementPattern(String statement){
        if(statement.matches("[a-zA-Z0-9]+\\.Write\\(\\(\\)=>\\{[a-zA-Z0-9]+\\.Remove\\([a-zA-Z0-9]+\\);\\}\\)"))
            return true;
        return false;
    }

    public ArrayList<String> RealmWriteForDeleteStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        String javaOutput = "";
        String swiftOutput = "";
        String objectName = statement.substring(0,statement.indexOf("."));
        String newSubString = statement.substring(statement.indexOf("{")+1,statement.lastIndexOf("}"));
        String queryName = newSubString.substring(newSubString.indexOf("(")+1,newSubString.lastIndexOf(")"));
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        javaOutput = javaRealmHandler.RealmWriteForDeleteStatementPattern(objectName,queryName);
        swiftOutput = swiftRealmHandler.RealmWriteForDeleteStatementPattern(objectName,queryName);
        output.add(javaOutput);
        output.add(swiftOutput);
        return output;
    }

    public boolean CheckRealmWriteForUpdateStatementPattern(String statement){
        if(statement.matches("[a-zA-Z0-9]+\\.Write\\(\\(\\)=>\\{([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?=(\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+(\\.[0-9]+)?);?)+\\}\\)"))
            return true;
        return false;
    }
    // realm.Write(()=>{davidsStrat.Price=1700345.56;})
    public ArrayList<String> RealmWriteForUpdateStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        String realmObjectName = statement.substring(0,statement.indexOf("."));
        String newSubString = statement.substring(statement.indexOf("=")+3,statement.lastIndexOf("}"));
        String objectName = newSubString.substring(0,newSubString.indexOf("."));
        String className = "";
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();

        String assignments[] = newSubString.split(";");

        for(int i=0;i<assignments.length;i++){

                fields.add(assignments[i].substring(assignments[i].indexOf(".")+1,assignments[i].indexOf("=")));
                values.add(assignments[i].substring(assignments[i].indexOf("=")+1));
         }

        String javaOutput = javaRealmHandler.RealmWriteForUpdateStatementPattern(realmObjectName,objectName,fields,values);
        String swiftOutput = swiftRealmHandler.RealmWriteForUpdateStatementPattern(realmObjectName,objectName,fields,values);

        output.add(javaOutput);
        output.add(swiftOutput);


        return output;
    }

    public boolean CheckRealmInitializeAppStatementPattern(String statement){
        if(statement.matches("var[a-zA-Z0-9]+=App\\.Create\\((\"([a-zA-Z0-9]+|\\s)+\"|[a-zA-Z0-9]+)\\)"))
                return true;
        return false;
    }

    public ArrayList<String> RealmInitializeAppStatementPattern(String statement){
        ArrayList<String>output = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        String identifier = statement.substring(3,statement.indexOf("="));
        String value = statement.substring(statement.lastIndexOf("("),statement.lastIndexOf(")"));
        String javaOutput = javaRealmHandler.RealmInitializeAppStatementPattern(identifier,value);
        String swiftOutput = swiftRealmHandler.RealmInitializeAppStatementPattern(identifier,value);
        output.add(javaOutput);
        output.add(swiftOutput);
        return output;
    }
    // varuser=awaitapp.LogInAsync(Credentials.Anonymous())
    public boolean CheckRealmLoginAnonymousStatementPattern(String statement){
        if(statement.matches("var[a-zA-Z0-9]+=(await)?[a-zA-Z0-9]+\\.LogInAsync\\(Credentials\\.Anonymous\\(\\)\\)"))
            return true;
        return false;
    }

    public ArrayList<String> RealmLoginAnonymousStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        String identifier = statement.substring(3,statement.indexOf("="));
        String objectName = "";
        if(statement.contains("await")){
            objectName = statement.substring(statement.indexOf("await")+5,statement.indexOf("."));

        }
        else{
            objectName = statement.substring(statement.indexOf("=")+1,statement.indexOf("."));
        }

        String javaOutput = javaRealmHandler.RealmLoginAnonymousStatementPattern(identifier,objectName);
        String swiftOutput = swiftRealmHandler.RealmLoginAnonymousStatementPattern(identifier,objectName);
        output.add(javaOutput);
        output.add(swiftOutput);
        return output;
    }

 //   user.LogOutAsync();
    // awaituser.LogOutAsync()
    public boolean CheckRealmLogoutStatementPattern(String statement){
        if(statement.matches("(await)?[a-zA-Z0-9]+\\.LogOutAsync\\(\\)"))
            return true;
        return false;
    }

    public ArrayList<String> RealmLogoutStatementPattern(String statement){
        ArrayList<String> output = new ArrayList<>();
        JavaRealmHandler javaRealmHandler = new JavaRealmHandler();
        SwiftRealmHandler swiftRealmHandler = new SwiftRealmHandler();
        String identifier = "";
        if(statement.contains("await"))
                identifier = statement.substring(5,statement.indexOf("."));
        identifier= statement.substring(0,statement.indexOf("."));
        String javaOutput = javaRealmHandler.RealmLogoutStatementPattern(identifier);
        String swiftOutput = swiftRealmHandler.RealmLogoutStatementPattern(identifier);
        output.add(javaOutput);
        output.add(swiftOutput);
        return output;
    }

}
