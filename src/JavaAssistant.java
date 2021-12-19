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
    private static final Map<String, String> DATA_TYPES_HASH_MAP;
    public static final Map<String, Map<String, String>> mFirestoreObservers;
    private static final Map<String, String> mUserInterfaceDataTypesHashMap;
    private static final Map<String, Map<String, String>> mUserInterfaceObservers;
    private static final Map<String, Map<String, String>> JAVA_FUNCTIONS_RETURN_TYPES;
    private static final Map<String, Map<String, String>> mStaticFunctionsInSwift;
    private static final Map<String, Map<String, String>> mFunctionsInSwift;
    private ArrayList<String> targetSwiftMethods;
    public static boolean isFirestoreClosure = false;
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
        HashMap<String, Map<String, String>> temp9;
        HashMap<String, Map<String, String>> temp12;
        HashMap<String, Map<String, String>> temp11;
        HashMap<String, Map<String, String>> temp3;

        HashMap<String, String> temp10;

        HashMap<String, Map<String, String>> temp5;

        Gson gson = new GsonBuilder().create();


        Reader reader = new InputStreamReader((getFileFromResources("DataTypes.json")));

        Reader reader10 = new InputStreamReader((getFileFromResources("UserInterfaceDataTypes.json")));
        Reader reader11 = new InputStreamReader((getFileFromResources("JavaFunctionsReturnTypes.json")));
        Reader reader5 = new InputStreamReader((getFileFromResources("StaticBuiltInFunctionData.json")));
        Reader reader3 = new InputStreamReader((getFileFromResources("functionsInSwift.json")));
        Reader reader9 = new InputStreamReader((getFileFromResources("UIObserverToTarget.json")));
        Reader reader12 = new InputStreamReader((getFileFromResources("FirestoreObserverToTarget.json")));

        temp = gson.fromJson(reader, HashMap.class);
        temp5 = gson.fromJson(reader5, HashMap.class);
        temp3 = gson.fromJson(reader3, HashMap.class);
        temp9 = gson.fromJson(reader9, HashMap.class);
        temp10 = gson.fromJson(reader10, HashMap.class);
        temp11 = gson.fromJson(reader11, HashMap.class);
        temp12 = gson.fromJson(reader12, HashMap.class);
        DATA_TYPES_HASH_MAP = Collections.unmodifiableMap(temp);
        mFirestoreObservers = Collections.unmodifiableMap(temp12);
        mUserInterfaceObservers = Collections.unmodifiableMap(temp9);
        mUserInterfaceDataTypesHashMap = Collections.unmodifiableMap(temp10);
        JAVA_FUNCTIONS_RETURN_TYPES = Collections.unmodifiableMap(temp11);
        mStaticFunctionsInSwift = Collections.unmodifiableMap(temp5);
        mFunctionsInSwift = Collections.unmodifiableMap(temp3);


    }
    public static InputStream getFileFromResources(String fileName) {

        InputStream is = JavaAssistant.class.getClassLoader().getResourceAsStream(fileName);


        return is;
    }

    public  String getDataType(String xamrinDataType){
        System.out.println("333333  " + xamrinDataType);
        if(DATA_TYPES_HASH_MAP.containsKey(xamrinDataType))
            System.out.println(" 444444   " + DATA_TYPES_HASH_MAP.get(xamrinDataType));
        return  DATA_TYPES_HASH_MAP.get(xamrinDataType);
    }

}
