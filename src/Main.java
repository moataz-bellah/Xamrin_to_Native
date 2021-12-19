import generatedantlr.CSharpParser;
import generatedantlr.CSharpLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.lang.String;


public class Main {
    public static boolean terminated;
    public static void main(String[] args) {
        terminated = false;

        //file class
        ReadFile read = new ReadFile();

        //choose a file
        String swiftpath = "App.txt";
//        String swiftpath = "";
//        swiftpath = read.chooseFile();

        //read a file
        String swiftCode = read.IosFileRead(swiftpath);
//        String swiftCode = "struct ContentView: View {\n" +
//                "    @State private var name: String = \"\"\n" +
//                "@State private var age:String = \"\"\n" +
//                "    @State private var fullText: String = \"This is some editable text...\"\n" +
//                "\t@state private var new: String = \"BLABLABLA\"\n" +
//                "\n" +
//                "    var body: some let View {        \t\n" +
//                "\t   TextField(\"Enter your name\", text: name)\n" +
//                "\t   TextField(\"What is your age\", text: age)\n" +
//                "\t   TextEditor(text: fullText)\n" +
//                "\t   TextEditor(text: new)\n" +
//                "           Link(\"LearnSwiftUI\", destination: URL(string: \"https://www.hackingwithswift.com/quick-start/swiftui\")!)\n" +
//                "\tLink(\"google\",destination: URL(string: \"https://www.google.com\")!)\n" +
//                "\t   Image(\"foo\")\n" +
//                "\tImage(\"sky\")\n" +
//                "           Alert(title: Text(\"Title of the alert\"), message: Text(\"Alert Example\"), dismissButton: .default(Text(\"OK\")))\n" +
//                "Alert(title: Text(\"Title of the alert\"), message: Text(\"Alert 2\"), dismissButton: .default(Text(\"OK\")))\t\n" +
//                "\t\t     \t\t\t\t        \n" +
//                "    }\n" +
//                "}";
//

        String javaCode = convertToJava(swiftCode);
//        System.out.println(javaCode);

        //create a file

        read.IosFileWrite(javaCode);
        terminated = true;

    }

    private static String convertToJava(String swiftCode) {
        XamarinVisitor swiftToJavaVisitor = new XamarinVisitor();
//        System.out.println("1");
        CSharpParser parser = new CSharpParser(null);
//        System.out.println("2");
        ANTLRInputStream input = new ANTLRInputStream(swiftCode);
//        System.out.println("3");
        CSharpLexer lexer = new CSharpLexer(input);
//        System.out.println("4");
        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        System.out.println("5");
        parser.setInputStream(tokens);
//        System.out.println("6");
        // from here u need to change your code

        ParseTree parseTree = parser.compilation_unit();
//        System.out.println("7");
        Object OjavaCode =  swiftToJavaVisitor.visitCompilation_unit((CSharpParser.Compilation_unitContext) parseTree);
//        System.out.println("8");
        String javaCode = OjavaCode.toString();
        return javaCode;
    }
}
