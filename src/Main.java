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


        //read a file
        String swiftCode = read.IosFileRead(swiftpath);


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
