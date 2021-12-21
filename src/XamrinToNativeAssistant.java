import java.util.HashMap;
import java.util.Map;

public class XamrinToNativeAssistant {
    public static HashMap<String,String> currentMethodVariables;
    public static HashMap<String,HashMap<String,String>> classesAttributes = new HashMap<>();
    XamrinToNativeAssistant(){
       // classesAttributes = new HashMap<>();
    }
    public static boolean isVariableFoundInCurrentMethod(String variable){
        if(currentMethodVariables.containsKey(variable))
            return true;
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

}
