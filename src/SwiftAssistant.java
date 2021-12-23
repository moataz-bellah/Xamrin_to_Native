import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SwiftAssistant {

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
    private static  final Map<String, String> SWIFT_DATA_TYPES_HASH_MAP;
    private static  final Map<String, Map<String, String>> SWIFT_FUNCTIONS_RETURN_TYPES;
    private static  final Map<String, Map<String, String>> mXamrinStaticFunctionsInSwift;
    private static  final Map<String, Map<String, String>> mXamrinFunctionsInSwift;
    private ArrayList<String> targetSwiftMethods;
    public static boolean isFirestoreClosure = false;
    //private ArrayList<String> currentSwiftImagePickers;


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

    public SwiftAssistant() {
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

        Reader reader2 = new InputStreamReader((getFileFromResources("SwiftDataTypes.json")));
        Reader reader12 = new InputStreamReader((getFileFromResources("SwiftFunctionsReturnTypes.json")));
        Reader reader6 = new InputStreamReader((getFileFromResources("SwiftStaticBuiltInFunctionData.json")));
        Reader reader4 = new InputStreamReader((getFileFromResources("XamrinFunctionsInSwift.json")));

        temp2 = gson.fromJson(reader2, HashMap.class);
        temp6 = gson.fromJson(reader6, HashMap.class);

        temp4 = gson.fromJson(reader4, HashMap.class);

        temp12 = gson.fromJson(reader12, HashMap.class);


        SWIFT_DATA_TYPES_HASH_MAP = Collections.unmodifiableMap(temp2);
        SWIFT_FUNCTIONS_RETURN_TYPES = Collections.unmodifiableMap(temp12);
        mXamrinStaticFunctionsInSwift = Collections.unmodifiableMap(temp6);
        mXamrinFunctionsInSwift = Collections.unmodifiableMap(temp4);

    }
    public static InputStream getFileFromResources(String fileName) {

        InputStream is = SwiftAssistant.class.getClassLoader().getResourceAsStream(fileName);


        return is;
    }

    public String getDataType(String xamrinDataType){
        if(SWIFT_DATA_TYPES_HASH_MAP.containsKey(xamrinDataType))
            return SWIFT_DATA_TYPES_HASH_MAP.get(xamrinDataType);
        return "";
    }

}
