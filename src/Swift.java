import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class Swift {

    public String variable_declaration(Map<String,String> csVariableDeclaration){
        // state is about var or let
        String varExpression = "";
        varExpression = csVariableDeclaration.get("state") + " "+ csVariableDeclaration.get("varName") + " :" + csVariableDeclaration.get("data_type")   ;
        if(csVariableDeclaration.containsKey("expression")){
            varExpression+=(" = " + csVariableDeclaration.get("expression"));
        }

        return varExpression + "\n";
    }

    public String variable_declaration_without_datatype(Map<String,String> csVariableDeclaration){
        String varExpression = "";
        varExpression =  "var  " + csVariableDeclaration.get("varName");
        if(csVariableDeclaration.containsKey("expression")){
            varExpression+=(" = " + csVariableDeclaration.get("expression"));
        }
        varExpression+=";";
        return varExpression + "\n";

    }
    public String expression(Map<String,String> csExpression){
        String expression  = csExpression.get("expression") + ";";
        return expression;
    }
    public String if_statement(Map<String,String> if_expression){
        String expression = "if(" + if_expression.get("expression") + ")";
        return expression;
    }

    public String elseif_statement(Map<String,String> if_expression){
        String expression = "else if(" + if_expression.get("expression") + ")";
        return expression;
    }

    public String else_statement(){
        return "else ";
    }
    public String open_brace(){
        return "{ \n";
    }
    public String close_brace(){
        return "} \n";
    }
    public String open_paranc(){return "(";}
    public String close_paranc(){return ")";}
    public String open_bracket(){return "[";}
    public String close_bracket(){return "]";}
    public String newLine(){return "\n";}
    public String openTag(){return "<";}
    public String closeTag(){return ">";}

    public String function_declaration(Map<String,String> functionDeclaration, ArrayList<String> functionParameters){
        String expression = functionDeclaration.get("accessModifier") + " func "  + functionDeclaration.get("functionName");
        expression+="( ";
        for(String param : functionParameters){
            expression+=param + ",";
        }
        if(expression.endsWith(",")){
            expression = expression.substring(0,expression.lastIndexOf(','));
        }
        expression += ")";
        expression+=("->"+functionDeclaration.get("dtype"));
        return expression;
    }
    public String class_declaration(Map<String,String> classDeclaration){
        String expression = classDeclaration.get("accessModifier") + " class " + classDeclaration.get("className");
        if(classDeclaration.containsKey("inherit")){
            expression+=(" : " + classDeclaration.get("inherit"));
        }
        return expression;
    }
    public String constructor_declaration(Map<String,String> constructorStatement, ArrayList<String> functionParameters){
        String expression = " init ";
        expression+="( ";
        for(String param : functionParameters){
            expression+=param + ",";
        }
        if(expression.endsWith(",")){
            expression = expression.substring(0,expression.lastIndexOf(','));
        }
        expression += ")";
        return expression;
    }
    public String return_statement(Map<String,String> returnStatement){
        return "return " + returnStatement.get("returnStatement");
    }

    public String import_statement(Map<String,String> importStatement){
        String expression  =  "import " + importStatement.get("import") + ";";
        return expression;
    }

    public String assignment_expression(Map<String,String> assignmentExpression){
        String expreesion = assignmentExpression.get("leftExpression") + " = " + assignmentExpression.get("rightExpression");
        return expreesion + ";";
    }
    public String expression_operator_expression(Map<String,String> ExpOpExp){

        String expression = ExpOpExp.get("leftExpression") + " " + ExpOpExp.get("operator") + " " + ExpOpExp.get("rightExpression");
        return expression;

    }

    public String for_loop_statement(Map<String,String>forLoop){
        String expression = "for(" + forLoop.get("start") + ";" + forLoop.get("end") + ";" + forLoop.get("increment") + ")";
        return expression;
    }
    public String while_loop_statement(Map<String,String>whileLoop){
        String expression = "while(" + whileLoop.get("condition") + ")";
        return expression;
    }

    public String for_in_statement(Map<String,String>forIN){
        String expression = "for(";
        if(forIN.containsKey("dtype")){
            expression+=forIN.get("dtype");
        }
        else{
            expression+="var ";
        }
        expression+=(forIN.get("iterative") + " : " + forIN.get("data") + ")");
        return expression;
    }
    public String array_declaration(Map<String,String> arrayDeclaration,String elements){

        // var emptyDoubles: [Double] = []
        String expression = "var " + arrayDeclaration.get("varName") + ": [" + arrayDeclaration.get("data_type") + "]";

        if(elements!=""){
            expression+=(" = [" + elements + "]");
        }
        else{
            expression+=(" = []");
        }
        return expression+ ";\n";
    }
    public String append(Map<String,String> data){
        return data.get("name") + ".append(" + data.get("element") + ");\n";
    }
    public String array_list_declaration(Map<String,String> arrayList){
        String expression = "ArrayList<";
        if(arrayList.containsKey("genericDataType")){
            expression+=arrayList.get("genericDataType");
        }
        expression+=(">" + arrayList.get("arrayName"));
        return expression;
    }

    public String map_declaration(Map<String,String> mapDeclaration){
        // var emptyDictionary =  [Int: String]()
        String expression =  "var " + mapDeclaration.get("mapName") + " = [" + mapDeclaration.get("key") + ": "  + mapDeclaration.get("value") + "]()";
        return expression + "\n";
    }

    public String hash_map_declaration(Map<String,String> mapDeclaration){
        String expression = ("HashMap<" + mapDeclaration.get("key") + "," + mapDeclaration.get("value") + mapDeclaration.get(">"));
        expression+=mapDeclaration.get("mapName");

        return expression;
    }

    public String stack_declaration(Map<String,String> stackDeclaration){
        String expression = "Stack<" + stackDeclaration.get("dtype") + ">" + stackDeclaration.get("stackName");
        return expression;
    }

     public String casting_statement(Map<String,String> castingStatement){
        String expression = "(" + castingStatement.get("dtype") + ") " + castingStatement.get("expression");
        return expression;
    }

    //TODO i will implement it later
    public String enum_declaretion(Map<String,String> enumDeclaration){

        return "";
    }

    //TODO i will implement it later
    public String async_statement(Map<String,String> asyncStatement){
        return "";
    }



}
