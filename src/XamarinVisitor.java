import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import generatedantlr.CSharpParser;
import generatedantlr.CSharpParserBaseVisitor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLOutput;
import java.util.*;

/** There is a problem in Java Assistant  Class **/


public  class XamarinVisitor extends CSharpParserBaseVisitor {
    Java JavaObject;
    Swift SwiftObject;
    JavaAssistant mJavaAssistant;

    public XamarinVisitor(){
        JavaObject  = new Java();
        SwiftObject = new Swift();
        //mJavaAssistant = new JavaAssistant();
    }




    @Override
    public ArrayList<String> visitCompilation_unit(CSharpParser.Compilation_unitContext ctx) {
        ArrayList<String> output = visitNamespace_member_declarations(ctx.namespace_member_declarations());
        System.out.println("SUIIIIIIIIIIIII Java Code " + output.get(0));
        System.out.println("SUIIIIIIIIIIIII Swift Code " + output.get(1));
        return output;
    }

    @Override
    public ArrayList<String> visitNamespace_member_declarations(CSharpParser.Namespace_member_declarationsContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        String javaCode  = "";
        String swiftCode = "";

        for(CSharpParser.Namespace_member_declarationContext i  : ctx.namespace_member_declaration()){
             ArrayList <String>temp = visitNamespace_member_declaration(i);
             javaCode += temp.get(0);
             swiftCode+=temp.get(1);

        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitNamespace_member_declaration(CSharpParser.Namespace_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.type_declaration()!=null)
            output = visitType_declaration(ctx.type_declaration());
        else if(ctx.namespace_declaration() != null)
            output = visitNamespace_declaration(ctx.namespace_declaration());
        return output;
    }

    @Override
    public ArrayList<String> visitNamespace_declaration(CSharpParser.Namespace_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output = visitNamespace_body(ctx.namespace_body());
        return  output;
    }

    @Override
    public ArrayList<String> visitNamespace_body(CSharpParser.Namespace_bodyContext ctx) {
        ArrayList<String> output = visitNamespace_member_declarations(ctx.namespace_member_declarations());
        return output;
    }

    @Override
    public ArrayList<String> visitType_declaration(CSharpParser.Type_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output = visitClass_definition(ctx.class_definition());
        return output;
    }

    @Override
    public ArrayList<String> visitClass_definition(CSharpParser.Class_definitionContext ctx) {
        String swiftCode = "";
        String javaCode = "";

        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String,String> classDeclaration;
        classDeclaration = new HashMap<>();
        classDeclaration.put("className",ctx.identifier().getText().toString());
        classDeclaration.put("accessModifier","");
        //classDeclaration.put("inherit","");
        swiftCode+=SwiftObject.class_declaration(classDeclaration);
        javaCode+=JavaObject.class_declaration(classDeclaration);
        swiftCode+=SwiftObject.open_brace();
        javaCode+=JavaObject.open_brace();

        temp = visitClass_body(ctx.class_body());
        javaCode+=temp.get(0);
        swiftCode+=temp.get(1);
        swiftCode+=SwiftObject.close_brace();
        javaCode+=JavaObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitClass_body(CSharpParser.Class_bodyContext ctx) {
        ArrayList<String> output = visitClass_member_declarations(ctx.class_member_declarations());
        return output;
    }

    @Override
    public ArrayList<String> visitClass_member_declarations(CSharpParser.Class_member_declarationsContext ctx) {
       ArrayList<String> output = new ArrayList<>();
       ArrayList<String> temp  = new ArrayList<>();
       String javaCode  = "";
       String swiftCode = "";
       if(ctx.class_member_declaration()!=null) {
            for (CSharpParser.Class_member_declarationContext i : ctx.class_member_declaration()) {
                temp = visitClass_member_declaration(i);
                javaCode+=temp.get(0);
                swiftCode+=temp.get(1);

            }
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitClass_member_declaration(CSharpParser.Class_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output = visitCommon_member_declaration(ctx.common_member_declaration());
        return  output;
    }

    @Override
    public ArrayList<String> visitCommon_member_declaration(CSharpParser.Common_member_declarationContext ctx) {
        ArrayList<String> output = visitTyped_member_declaration(ctx.typed_member_declaration());
        return output;
    }

    @Override
    public ArrayList<String> visitTyped_member_declaration(CSharpParser.Typed_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode  = "";
        String swiftCode = "";
        HashMap<String,String> javaVariableDeclaration = new HashMap<>();
        HashMap<String,String> swiftVariableDeclaration = new HashMap<>();
        javaVariableDeclaration.put("data_type","int");
        temp = visitField_declaration(ctx.field_declaration());
        javaVariableDeclaration.put("varName",temp.get(0));
        swiftVariableDeclaration.put("data_type","Int"); // Later we will use Java Assistant when it is fixed
        swiftVariableDeclaration.put("varName",temp.get(1));
        swiftVariableDeclaration.put("state","var");
        javaCode+=JavaObject.variable_declaration(javaVariableDeclaration);
        swiftCode+=SwiftObject.variable_declaration(swiftVariableDeclaration);
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitField_declaration(CSharpParser.Field_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output=visitVariable_declarators(ctx.variable_declarators());
        return output;
    }

    @Override
    public ArrayList<String> visitVariable_declarators(CSharpParser.Variable_declaratorsContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode  = "";
        String swiftCode = "";
        for(CSharpParser.Variable_declaratorContext i : ctx.variable_declarator()){
            temp = visitVariable_declarator(i);
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitVariable_declarator(CSharpParser.Variable_declaratorContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output.add(ctx.identifier().getText());
        output.add(ctx.identifier().getText());
        return output;
    }
}