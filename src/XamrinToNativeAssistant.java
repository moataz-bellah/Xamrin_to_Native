import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class XamrinToNativeAssistant {
    public static String currentDataType = "";
    public static boolean isMap = false;
    public static boolean isArray = false;
    public static boolean isArrayList = false;
    public static String arrayListName = "";
    public static String mapName = "";
    public static HashMap<String,String> currentMethodVariables = new HashMap<>();

    public static HashMap<String,HashMap<String,String>> classesAttributes = new HashMap<>();
    /** it returns map {"className":{"method(String)":"String"}}**/
    public static HashMap<String, HashMap<String, String>> classFunctionsArgumentsDataType =new HashMap<>();
    public static String localVariableTmpDatatype = new String();



    XamrinToNativeAssistant(){
       // classesAttributes = new HashMap<>();
    }
    public static boolean isVariableFoundInCurrentMethod(String variable){

        if(currentMethodVariables.containsKey(variable)){

            return true;
        }
        else
            return false;
    }

    public static boolean isVariableFoundInCurrentClass(String className,String variable){
        if(classesAttributes.get(className).containsKey(variable))
            return true;
        else
            return false;
    }
    public static boolean isVariableFound(String variable){
        for(HashMap.Entry<String,HashMap<String,String>>entry:classesAttributes.entrySet()){
            String key = entry.getKey();
            HashMap<String,String> temp = entry.getValue();
            for(HashMap.Entry<String,String> entry2:temp.entrySet()){
                if(entry2.getKey() == variable)
                    return true;
            }
        }
        return false;
    }

    public static String getClassAttributeDataType(String className,String attribute){
        return classesAttributes.get(className).get(attribute);
    }

    public static String getCurrentMethodVariableDataType(String variable){
        return currentMethodVariables.get(variable);
    }


    public static void addNewVariable(String variable,String dataType){
        System.out.println("");
        currentMethodVariables.put(variable,dataType);
    }
    public static void addNewAttribute(String className,String attribute,String dataType){
        if(classesAttributes.containsKey(className)){
        classesAttributes.get(className).put(attribute,dataType);
        }
        else{
            HashMap<String,String> temp = new HashMap<>();
            temp.put(attribute,dataType);
            classesAttributes.put(className,temp);
        }
    }

    public static void addNewFunctionDefinition(String className,String functionName,String csharpParameters,String functionReturnType
    ){

        String arguments = "";
        String functionDefinition = functionName + "(";
        String[] parameters = csharpParameters.split(",");
        for(String i : parameters){
            String[] temp = i.split(" ");
            functionDefinition+=temp[0];
            functionDefinition+=",";
        }
        if(functionDefinition.endsWith(",")){
            functionDefinition = functionDefinition.substring(0,functionDefinition.lastIndexOf(','));
        }
        functionDefinition+=")";
        addNewVariableFunction(className,functionDefinition,functionReturnType);

    }

    public static void addNewVariableFunction(String className,String functionDefinition,String functionReturnType) {
        if (classFunctionsArgumentsDataType.containsKey(className)) {

            classFunctionsArgumentsDataType.get(className).put(functionDefinition, functionReturnType);
        } else {
            HashMap<String, String> temp = new HashMap<>();
            temp.put(functionDefinition, functionReturnType);
            classFunctionsArgumentsDataType.put(className, temp);

        }
    }
    public static void addTmpLocalVariableDatatype(String Datatype) {
        localVariableTmpDatatype = Datatype;
    }

    public static String checkDataTypeOfVariable(String variable){

        if(getCurrentMethodVariableDataType(variable) != null){
            return getCurrentMethodVariableDataType(variable);
        }
        else if(variable.contains("\"")) {

            return "String";

        }
        else if(variable.contains("."))
            return "Float";
        else if(variable == "")
            return "";
        return "int";
    }

}
