import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JavaAssistant {


    private static HashMap<String, HashMap<String, ArrayList<String>>> functionArgumentNames;

    /**
     * maps the ID of an object in the layout.xml to its java object name
     * to be used later in converting the android layout to scene in IOS
     */

    private HashMap<String, String> objectsUIIDs;

    /**
     * this is a pair used to keep the info  (HashmapName,<key data type, value data type>) for the currently being
     * converted map/hashmap into a dictionary in swift
     */

    private static HashMap <Integer, HashMap<String, String>> convertedHashMap;
    private String mJavaClassName;
    private String mJavaParentClassName;
    private static  final Map<String, String> JAVA_DATA_TYPES_HASH_MAP;
    private static  final Map<String, Map<String, String>> JAVA_FUNCTIONS_RETURN_TYPES;
    private static  final Map<String, Map<String, String>> mXamrinStaticFunctionsInJava;
    private static  final Map<String, Map<String, String>> mXamrinFunctionsInJava;
    private ArrayList<String> targetSwiftMethods;
    public static boolean isFirestoreClosure = false;
    public  String lastFunctionReturnType = "";
    //private ArrayList<String> currentSwiftImagePickers;
    private String mLayoutName;

    /**
     * this is a map to keep the mapping between imported classes and their parent packages
     */
    private static HashMap<String, String> importedLibraries;

    /**
     * used to store objects data types Key is variable name and value is its data type and its value
     */
    private Map<String, String> mIdentifiersTypes;
    /**
     * used to store class hierarchy Key is class name and value is its parent
     */
//    private static final HashMap<String, String> mUsedClasses;







    public JavaAssistant() {
        objectsUIIDs = new HashMap<>();
        convertedHashMap = new HashMap<>();
        targetSwiftMethods = new ArrayList<>();
        mIdentifiersTypes = new HashMap<>();
    }

    static {

        importedLibraries = new HashMap<>();
  //      mUsedClasses = new HashMap<>();
        functionArgumentNames = new HashMap<>();
        HashMap<String, String> temp;
        HashMap<String, String> temp2;
        HashMap<String, Map<String, String>> temp9;
        HashMap<String, Map<String, String>> temp12;
        HashMap<String, Map<String, String>> temp11;
        HashMap<String, Map<String, String>> temp3;
        HashMap<String, Map<String, String>> temp4;

        HashMap<String, String> temp10;
        HashMap<String, Map<String, String>> temp5;
        HashMap<String, Map<String, String>> temp6;
        Gson gson = new GsonBuilder().create();


        Reader reader = new InputStreamReader((getFileFromResources("JavaDataTypes.json")));
        Reader reader11 = new InputStreamReader((getFileFromResources("JavaFunctionsReturnTypes.json")));
        Reader reader5 = new InputStreamReader((getFileFromResources("JavaStaticBuiltInFunctionData.json")));
        Reader reader3 = new InputStreamReader((getFileFromResources("XamrinFunctionsInJava.json")));

        temp = gson.fromJson(reader, HashMap.class);
        temp5 = gson.fromJson(reader5, HashMap.class);
        temp3 = gson.fromJson(reader3, HashMap.class);
        temp11 = gson.fromJson(reader11, HashMap.class);


        JAVA_DATA_TYPES_HASH_MAP = Collections.unmodifiableMap(temp);
        JAVA_FUNCTIONS_RETURN_TYPES = Collections.unmodifiableMap(temp11);
        mXamrinStaticFunctionsInJava = Collections.unmodifiableMap(temp5);
        mXamrinFunctionsInJava = Collections.unmodifiableMap(temp3);

    }
    public static InputStream getFileFromResources(String fileName) {

        InputStream is = JavaAssistant.class.getClassLoader().getResourceAsStream(fileName);


        return is;
    }

    public  String getDataType(String xamrinDataType){

        if(JAVA_DATA_TYPES_HASH_MAP.containsKey(xamrinDataType))
              return  JAVA_DATA_TYPES_HASH_MAP.get(xamrinDataType);

        return "";
    }

    public String getStaticFunction(String xamarinClass,String xamarinMethod){

        if(mXamrinStaticFunctionsInJava.containsKey(xamarinClass))
            if(mXamrinStaticFunctionsInJava.get(xamarinClass).containsKey(xamarinMethod))
                return mXamrinStaticFunctionsInJava.get(xamarinClass).get(xamarinMethod);
        return "";

    }
    public String getFunctionReturnType(String className,String functionDefinition){
        if(JAVA_FUNCTIONS_RETURN_TYPES.get(className).containsKey(functionDefinition))
            return JAVA_FUNCTIONS_RETURN_TYPES.get(className).get(functionDefinition);
        return "";
    }
    public String getFunctionInJava(String lastFunctionReturnType,String functionName){

        if(mXamrinFunctionsInJava.get(lastFunctionReturnType).containsKey(functionName))
          return  mXamrinFunctionsInJava.get(lastFunctionReturnType).get(functionName);
        return "";
    }


}
