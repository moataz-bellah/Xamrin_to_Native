import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import generatedantlr.CSharpParser;
import generatedantlr.CSharpParserBaseVisitor;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Array;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import patterns.PatternsMatcher;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

import java.io.FileWriter;
import java.io.IOException;
public class XamarinVisitor extends CSharpParserBaseVisitor {

    public static String currentClass;
    public static String currentMethod;

    Java JavaObject;
    Swift SwiftObject;
    JavaAssistant mJavaAssistant;
    SwiftAssistant mSwiftAssistant;
    PatternsMatcher patternsMatcher;
    public XamarinVisitor(){
        JavaObject  = new Java();
        SwiftObject = new Swift();
        mJavaAssistant = new JavaAssistant();
        mSwiftAssistant = new SwiftAssistant();
        patternsMatcher = new PatternsMatcher();
    }




    @Override
    public ArrayList<String> visitCompilation_unit(CSharpParser.Compilation_unitContext ctx) {
        ArrayList<String> output = visitNamespace_member_declarations(ctx.namespace_member_declarations());

        System.out.println("Java Code\n" + output.get(0));
        System.out.println("Swift Code\n" + output.get(1));
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

        HashMap<String,String> javaClassDeclaration;
        HashMap<String,String> swiftClassDeclaration = new HashMap<>();

        javaClassDeclaration = new HashMap<>();
        currentClass = ctx.identifier().getText();
        javaClassDeclaration.put("className",ctx.identifier().getText().toString());
        javaClassDeclaration.put("accessModifier","");

        swiftClassDeclaration.put("className",ctx.identifier().getText());
        swiftClassDeclaration.put("accessModifier","");
        //classDeclaration.put("inherit","");



        if(ctx.class_base() != null){

            temp = visitClass_base(ctx.class_base());
            javaClassDeclaration.put("inherit",temp.get(0));
            swiftClassDeclaration.put("inherit",temp.get(1));
        }
        swiftCode+=SwiftObject.class_declaration(swiftClassDeclaration);
        javaCode+=JavaObject.class_declaration(javaClassDeclaration);
        swiftCode+=SwiftObject.open_brace();
        javaCode+=JavaObject.open_brace();
        temp = visitClass_body(ctx.class_body());
        javaCode+=temp.get(0);
        swiftCode+=temp.get(1);
        swiftCode+=SwiftObject.close_brace();
        javaCode+=JavaObject.close_brace();
        try {
            FileWriter javaFile = new FileWriter(currentClass + ".java");
            FileWriter swiftFile = new FileWriter(currentClass + ".swift");
            javaFile.write(javaCode);
            swiftFile.write(swiftCode);
            javaFile.close();
            swiftFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
    public ArrayList<String> visitClass_base(CSharpParser.Class_baseContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        String parentClasses = "";
        String javaParentClass = "";
        String swiftParentClass = "";
        if(ctx.class_type() != null){

            if(ctx.class_type().namespace_or_type_name() != null){
                 javaParentClass = mJavaAssistant.getDataType(ctx.class_type().namespace_or_type_name().getText());
                 swiftParentClass = mSwiftAssistant.getDataType(ctx.class_type().namespace_or_type_name().getText());

                //parentClasses+=(ctx.class_type().namespace_or_type_name().getText()+",");

            }


        }

        for(CSharpParser.Namespace_or_type_nameContext c : ctx.namespace_or_type_name()){
            javaParentClass = mJavaAssistant.getDataType(c.getText()) + ",";
            swiftParentClass = mSwiftAssistant.getDataType(c.getText()) + ",";
           //     parentClasses+=(c.getText()+",");
        }
        if(javaParentClass.endsWith(",")) {
            javaParentClass = javaParentClass.substring(0, javaParentClass.lastIndexOf(","));
            swiftParentClass = swiftParentClass.substring(0, swiftParentClass.lastIndexOf(","));
        }
        output.add(javaParentClass);
        output.add(swiftParentClass);
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
       else{
           System.out.println("ssssss");
       }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitClass_member_declaration(CSharpParser.Class_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";

        if(patternsMatcher.CheckGettersAndSettersStatementPattern2(ctx.getText())) {

            output = patternsMatcher.GettersAndSettersStatementPattern2(ctx.getText());
            return output;
        }
        if(ctx.all_member_modifiers()!=null){
                javaCode+=(ctx.all_member_modifiers().getText() + " ");
                swiftCode+=(ctx.all_member_modifiers().getText() + " ");
        }


        temp = visitCommon_member_declaration(ctx.common_member_declaration());
        javaCode+=temp.get(0);
        swiftCode+=temp.get(1);
        output.add(javaCode);
        output.add(swiftCode);
        return  output;
    }

    @Override
    public ArrayList<String> visitCommon_member_declaration(CSharpParser.Common_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();

        if(ctx.method_declaration()!= null){
            output = visitMethod_declaration(ctx.method_declaration());
        }
        else if(ctx.typed_member_declaration()!=null){
            output = visitTyped_member_declaration(ctx.typed_member_declaration());
        }
        else if(ctx.constructor_declaration() != null){
            output = visitConstructor_declaration(ctx.constructor_declaration());
        }
        return output;
    }

    /** Constructor **/
    @Override
    public ArrayList<String> visitConstructor_declaration(CSharpParser.Constructor_declarationContext ctx) {
        String constructorName = ctx.identifier().getText();
        ArrayList<String> output = new ArrayList<>();
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();
        ArrayList<String> javaParameters = new ArrayList<>();
        ArrayList<String> swiftParameters = new ArrayList<>();
        String functionParametersInCSharp = "";
        ArrayList<String> methodBlock = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        XamrinToNativeAssistant.currentMethodVariables.clear();
        currentMethod = constructorName;
        javaMap.put("name",constructorName);
        swiftMap.put("name",constructorName);
        if(ctx.formal_parameter_list() != null){
            javaParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("javaCode");
            swiftParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("swiftCode");
            functionParametersInCSharp =  getFunctionParametersInCSharp(ctx.formal_parameter_list().fixed_parameters());
        }

        javaCode+=JavaObject.constructor_declaration(javaMap,javaParameters);
        swiftCode+=SwiftObject.constructor_declaration(swiftMap,swiftParameters);
        javaCode+=JavaObject.open_brace();
        swiftCode+=SwiftObject.open_brace();
        methodBlock = visitBody(ctx.body());
        javaCode+=methodBlock.get(0);
        swiftCode+=methodBlock.get(1);
        javaCode+=JavaObject.close_brace();
        swiftCode+=SwiftObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);
        // add function definition
        XamrinToNativeAssistant.addNewFunctionDefinition(currentClass,currentMethod,functionParametersInCSharp,XamrinToNativeAssistant.currentDataType);
        // clearing current data type
        XamrinToNativeAssistant.currentDataType = "";
        currentMethod = ""; // clearing current method name

        return output;
    }

    @Override
    public ArrayList<String> visitBody(CSharpParser.BodyContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        if(ctx.block() != null){
            temp = visitBlock(ctx.block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    /** Method Section **/




    @Override
    public ArrayList<String> visitMethod_declaration(CSharpParser.Method_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();
        ArrayList<String> javaParameters = new ArrayList<>();
        ArrayList<String> swiftParameters = new ArrayList<>();
        if(patternsMatcher.CheckGettersAndSettersStatementPattern(ctx.getText())){
             javaParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("javaCode");
             swiftParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("swiftCode");
             output = patternsMatcher.GettersAndSettersStatementPattern(ctx.getText(),ctx.method_member_name().getText(),
                     javaParameters,swiftParameters,XamrinToNativeAssistant.currentDataType);

            return output;
        }
        String functionParametersInCSharp = "";
        ArrayList<String> methodBlock = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        XamrinToNativeAssistant.currentMethodVariables.clear();

        currentMethod = ctx.method_member_name().getText();
        javaMap.put("functionName",ctx.method_member_name().getText());
        swiftMap.put("functionName",ctx.method_member_name().getText());
        javaMap.put("accessModifier","");
        swiftMap.put("accessModifier","");
        javaMap.put("dtype",mJavaAssistant.getDataType(XamrinToNativeAssistant.currentDataType));
        swiftMap.put("dtype",mSwiftAssistant.getDataType(XamrinToNativeAssistant.currentDataType));

        if(ctx.formal_parameter_list() != null){
            javaParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("javaCode");
            swiftParameters = visitFormal_parameter_list(ctx.formal_parameter_list()).get("swiftCode");
            functionParametersInCSharp =  getFunctionParametersInCSharp(ctx.formal_parameter_list().fixed_parameters());
        }

        javaCode+=JavaObject.function_declaration(javaMap,javaParameters);
        swiftCode+=SwiftObject.function_declaration(swiftMap,swiftParameters);
        methodBlock = visitMethod_body(ctx.method_body());
        javaCode+=methodBlock.get(0);
        swiftCode+=methodBlock.get(1);
        output.add(javaCode);
        output.add(swiftCode);
        // add function definition
        XamrinToNativeAssistant.addNewFunctionDefinition(currentClass,currentMethod,functionParametersInCSharp,XamrinToNativeAssistant.currentDataType);
        // clearing current data type
        XamrinToNativeAssistant.currentDataType = "";
        currentMethod = ""; // clearing current method name
        return output;
    }



    /** Method`s Parameters Section  **/

    @Override
    public HashMap<String,ArrayList<String>> visitFormal_parameter_list(CSharpParser.Formal_parameter_listContext ctx) {
        HashMap<String,ArrayList<String>> output = new HashMap<>();
        String functionParametersInCSharp =  getFunctionParametersInCSharp(ctx.fixed_parameters());
        XamrinToNativeAssistant.addNewFunctionDefinition(currentClass,currentMethod,functionParametersInCSharp,XamrinToNativeAssistant.currentDataType);
        output = visitFixed_parameters(ctx.fixed_parameters());

        return output;
    }


    @Override
    public HashMap<String,ArrayList<String>> visitFixed_parameters(CSharpParser.Fixed_parametersContext ctx) {
        HashMap<String,ArrayList<String>> output = new HashMap<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> javaParameters = new ArrayList<>();
        ArrayList<String> swiftParameters = new ArrayList<>();

        for(CSharpParser.Fixed_parameterContext i : ctx.fixed_parameter()){
                temp = visitFixed_parameter(i);
                javaParameters.add(temp.get(0));
                swiftParameters.add(temp.get(1));
        }
        output.put("javaCode",javaParameters);
        output.put("swiftCode",swiftParameters);
        return output;
    }

    /** Get Method Parameters in C#  **/

    public String getFunctionParametersInCSharp(CSharpParser.Fixed_parametersContext ctx){
       String parametersInCSharp = "";
        for(CSharpParser.Fixed_parameterContext i : ctx.fixed_parameter()){
           parametersInCSharp+= (i.arg_declaration().type_().getText() + " " +i.arg_declaration().identifier().getText());
            parametersInCSharp+=",";
        }
        if(parametersInCSharp.endsWith(",")){
            parametersInCSharp = parametersInCSharp.substring(0,parametersInCSharp.lastIndexOf(','));
        }
        return parametersInCSharp;
    }
    @Override
    public ArrayList<String> visitFixed_parameter(CSharpParser.Fixed_parameterContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output = visitArg_declaration(ctx.arg_declaration());
        return output;
    }


    @Override
    public ArrayList<String> visitArg_declaration(CSharpParser.Arg_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();

        String parameterID = ctx.identifier().getText();
        String parameterDataType = ctx.type_().getText(); // It will be edited
        javaMap.put("data_type",parameterDataType);
        javaMap.put("varName",parameterID);
        swiftMap.put("data_type",parameterDataType);
        swiftMap.put("varName",parameterID);
        swiftMap.put("state","var"); // for now
        String javaCode = JavaObject.variable_declaration(javaMap);
        String swiftCode = SwiftObject.variable_declaration(swiftMap);
        XamrinToNativeAssistant.addNewVariable(parameterID,parameterDataType);
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    /** End of Method`s Parameters Section  **/



    /** Method Body Section **/
    @Override
    public ArrayList<String> visitMethod_body(CSharpParser.Method_bodyContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        javaCode+=JavaObject.open_brace();
        swiftCode+=SwiftObject.open_brace();
        temp = visitBlock(ctx.block());
        javaCode+= temp.get(0);
        swiftCode+= temp.get(1);
        javaCode+=JavaObject.newLine();
        swiftCode+=SwiftObject.newLine();
        javaCode+=JavaObject.close_brace();
        swiftCode+=SwiftObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitBlock(CSharpParser.BlockContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.statement_list() != null){
            output = visitStatement_list(ctx.statement_list());
            return output;
        }
        /* if the body has no code like {
                                           }

         */
        output.add("");
        output.add("");
        return output;
    }

    @Override
    public ArrayList<String> visitStatement_list(CSharpParser.Statement_listContext ctx) {
        ArrayList<String> output= new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        for (CSharpParser.StatementContext i : ctx.statement()){

            javaCode += visitStatement(i).get(0);
            swiftCode += visitStatement(i).get(1);
        }
        output.add(javaCode);
        output.add(swiftCode);

        return output;
    }

        @Override
    public ArrayList<String> visitStatement(CSharpParser.StatementContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if (ctx.declarationStatement()!=null){
            if(ctx.declarationStatement().local_variable_declaration()!=null){
                output = visitLocal_variable_declaration(ctx.declarationStatement().local_variable_declaration());
            }
        }else if(ctx.embedded_statement()!=null){
            output = visitEmbedded_statement(ctx.embedded_statement());

        }
        return output;
    }

    @Override
    public ArrayList<String> visitEmbedded_statement(CSharpParser.Embedded_statementContext ctx) {
        ArrayList<String> output = new ArrayList<>();

        // IfStatementContext
        if(ctx.simple_embedded_statement().getText().contains("if")){

            output = (ArrayList<String>) visitIfStatement((CSharpParser.IfStatementContext) ctx.simple_embedded_statement());
        }
        else if(ctx.simple_embedded_statement().getClass().toString().contains("TryStatementContext")){

            output = visitTryStatement((CSharpParser.TryStatementContext) ctx.simple_embedded_statement());
        }
        else if(ctx.simple_embedded_statement().getClass().toString().contains("WhileStatementContext")){

            output = visitWhileStatement((CSharpParser.WhileStatementContext) ctx.simple_embedded_statement());
        }
        else if(ctx.simple_embedded_statement().getClass().toString().contains("ForStatementContext")){

                output = visitForStatement((CSharpParser.ForStatementContext) ctx.simple_embedded_statement());
        }
        else if(ctx.simple_embedded_statement().getClass().toString().contains("ExpressionStatement")){
            output = visitExpression((CSharpParser.ExpressionContext)ctx.simple_embedded_statement().getChild(0));
        }
        return output;
    }

    @Override
    public ArrayList<String> visitTryStatement(CSharpParser.TryStatementContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "try ";
        String swiftCode = "do ";
        javaCode+=JavaObject.open_brace();
        swiftCode+=SwiftObject.open_brace();
        if(ctx.block() != null){
            temp = visitBlock(ctx.block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        javaCode+=JavaObject.close_brace();
        swiftCode+=SwiftObject.close_brace();
        if(ctx.catch_clauses() != null){
            temp = visitCatch_clauses(ctx.catch_clauses());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }

        if(ctx.finally_clause() != null){
            temp = visitFinally_clause(ctx.finally_clause());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitCatch_clauses(CSharpParser.Catch_clausesContext ctx) {
        ArrayList output = new ArrayList();
        ArrayList temp = new ArrayList();

        if(ctx.specific_catch_clause() != null){
           output =  visitSpecific_catch_clause(ctx.specific_catch_clause(0));
        }
        return output;
    }

    @Override
    public ArrayList<String> visitSpecific_catch_clause(CSharpParser.Specific_catch_clauseContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        XamrinToNativeAssistant.addNewVariable(ctx.identifier().getText(),ctx.class_type().getText());
        String javaCode  = "catch ("+ ctx.class_type().getText()+ " " + ctx.identifier().getText() + ")";
        String swiftCode = "catch ";
        javaCode+=JavaObject.open_brace();
        swiftCode+=SwiftObject.open_brace();
        if(ctx.block() != null){
            temp = visitBlock(ctx.block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        javaCode+=JavaObject.close_brace();
        swiftCode+=SwiftObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);

        return output;
    }

    @Override
    public ArrayList<String> visitFinally_clause(CSharpParser.Finally_clauseContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp  = new ArrayList<>();
        String javaCode = "finally ";
        String swiftCode = "catch ";
        javaCode+=JavaObject.open_brace();
        swiftCode+=SwiftObject.open_brace();
        if(ctx.block() != null){
            temp = visitBlock(ctx.block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
        }
        javaCode+=JavaObject.close_brace();
        swiftCode+=SwiftObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitIfStatement(CSharpParser.IfStatementContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();
        temp = visitExpression(ctx.expression());

        javaMap.put("expression",temp.get(0));
        swiftMap.put("expression",temp.get(1));


        if(ctx.getText().substring(0,6).contains("elseif")){
            javaCode+=JavaObject.elseif_statement(javaMap);
            swiftCode+=SwiftObject.elseif_statement(swiftMap);
        }
        else {
            javaCode += JavaObject.if_statement(javaMap);
            //javaCode+=JavaObject.open_brace();
            swiftCode += SwiftObject.if_statement(swiftMap);
        }
        //        swiftCode+=SwiftObject.open_brace();
//        if (ctx.if_body()!= null){
//
//            temp = visitIf_body(ctx.if_body(0));
//        }


        for(CSharpParser.If_bodyContext c : ctx.if_body()){
            javaCode+=JavaObject.open_brace();


            swiftCode+=SwiftObject.open_brace();

            temp = visitIf_body(c);
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
            javaCode+=JavaObject.close_brace();
            swiftCode+=SwiftObject.close_brace();
        }
//        javaCode+=temp.get(0);
//        javaCode+=JavaObject.close_brace();
//        swiftCode+=temp.get(1);
//        swiftCode+=SwiftObject.close_brace();
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitIf_body(CSharpParser.If_bodyContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.block()!= null) {
            output = visitBlock(ctx.block());
            return output;
        }
        else if(ctx.simple_embedded_statement() != null){
            if(ctx.simple_embedded_statement().getClass().toString().contains("IfStatementContext")) {
                output = (ArrayList<String>) visitIfStatement((CSharpParser.IfStatementContext) ctx.simple_embedded_statement());
                return output;

            }

        }
        // empty body
        output.add("");
        output.add("");
        return output;
    }

    @Override
    public ArrayList<String> visitForStatement(CSharpParser.ForStatementContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        HashMap<String,String> javaMap= new HashMap<>();
        HashMap<String,String> swiftMap= new HashMap<>();
        if(ctx.for_initializer() != null){
            temp = visitFor_initializer(ctx.for_initializer());
            javaMap.put("start",temp.get(0));
            swiftMap.put("start",temp.get(1));
        }
        if(ctx.expression() != null){
            temp = visitExpression(ctx.expression());
            javaMap.put("end",temp.get(0));
            swiftMap.put("end",temp.get(1));
        }
        if(ctx.for_iterator() != null){
            temp = visitFor_iterator(ctx.for_iterator());
            javaMap.put("increment",temp.get(0));
            swiftMap.put("increment",temp.get(1));

        }
        javaCode+=JavaObject.for_loop_statement(javaMap);
        swiftCode+=SwiftObject.for_loop_statement(swiftMap);

        if(ctx.embedded_statement() != null){
            javaCode+=JavaObject.open_brace();
            swiftCode+=SwiftObject.open_brace();
//            temp = visitEmbedded_statement(ctx.embedded_statement());
             temp =  visitBlock(ctx.embedded_statement().block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
            javaCode+=JavaObject.close_brace();
            swiftCode+=SwiftObject.close_brace();
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitFor_initializer(CSharpParser.For_initializerContext ctx) {
        ArrayList<String> output = new ArrayList<>();

        if(ctx.local_variable_declaration() != null)
            output = visitLocal_variable_declaration(ctx.local_variable_declaration());
        return output;
    }

    @Override
    public ArrayList<String> visitFor_iterator(CSharpParser.For_iteratorContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.expression(0) != null){
            output = visitExpression(ctx.expression(0));
        }
        return output;
    }

    @Override
    public ArrayList<String> visitWhileStatement(CSharpParser.WhileStatementContext ctx) {
        ArrayList<String>output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        HashMap<String,String> javaMap= new HashMap<>();
        HashMap<String,String> swiftMap= new HashMap<>();
        if(ctx.expression() != null){
            temp = visitExpression(ctx.expression());
            javaMap.put("condition",temp.get(0));
            swiftMap.put("condition",temp.get(1));
        }
        javaCode+=JavaObject.while_loop_statement(javaMap);
        swiftCode+=SwiftObject.while_loop_statement(swiftMap);

        if(ctx.embedded_statement() != null){
            javaCode+=JavaObject.open_brace();
            swiftCode+=SwiftObject.open_brace();
            temp = visitBlock(ctx.embedded_statement().block());
            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
            javaCode+=JavaObject.close_brace();
            swiftCode+=SwiftObject.close_brace();
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitLocal_variable_declaration(CSharpParser.Local_variable_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        HashMap<String,String> data = new HashMap<>();



        // "var[a-zA-Z]+=[0-9]+"
        if(patternsMatcher.CheckRealmInitStatementPattern(ctx.getText())){
            output = patternsMatcher.RealmInitStatementPattern(ctx.getText());
            return output;
        }

        if(patternsMatcher.CheckRealmInitializeAppStatementPattern(ctx.getText())) {

            output = patternsMatcher.RealmInitializeAppStatementPattern(ctx.getText());
            return output;
        }
        if(patternsMatcher.CheckRealmLoginAnonymousStatementPattern(ctx.getText())){

            output = patternsMatcher.RealmLoginAnonymousStatementPattern(ctx.getText());

            return output;
        }
        if(patternsMatcher.CheckRealmRetrieveAllStatementPattern(ctx.getText())) {

            output = patternsMatcher.RealmRetrieveAllStatementPattern(ctx.getText());
            return output;
        }
        if(patternsMatcher.CheckRealmWhereLessThanStatementPattern(ctx.getText())){
           output = patternsMatcher.RealmWhereLessThanStatementPattern(ctx.getText());
           return output;
        }
        if(patternsMatcher.CheckRealmOrderByStatementPattern(ctx.getText())){

            output = patternsMatcher.RealmOrderByStatementPattern(ctx.getText());
            return output;
        }
        if(ctx.local_variable_type() != null){
                data = visitLocal_variable_type(ctx.local_variable_type());
                XamrinToNativeAssistant.localVariableTmpDatatype = data.get("Xamarin_type");
        }
        else
            XamrinToNativeAssistant.localVariableTmpDatatype = ctx.local_variable_type().getText();
        for (CSharpParser.Local_variable_declaratorContext i :ctx.local_variable_declarator()){

            output = visitLocal_variable_declarator(i);

        }

        return output;
    }

    @Override
    public HashMap<String,String> visitLocal_variable_type(CSharpParser.Local_variable_typeContext ctx) {
        return visitType_(ctx.type_());
    }

    @Override
    public HashMap<String,String> visitType_(CSharpParser.Type_Context ctx) {
       if(ctx.rank_specifier() != null)
           for(CSharpParser.Rank_specifierContext c:ctx.rank_specifier()){
               visitRank_specifier(c);
           }


        return visitBase_type(ctx.base_type());
    }

    @Override
    public HashMap<String,String> visitRank_specifier(CSharpParser.Rank_specifierContext ctx) {
        HashMap<String,String> data = new HashMap<>();

        if(ctx.OPEN_BRACKET() != null && ctx.CLOSE_BRACKET() != null)
            XamrinToNativeAssistant.isArray = true;
        return data;
    }

    @Override
    public HashMap<String,String> visitBase_type(CSharpParser.Base_typeContext ctx) {
        if(ctx.simple_type()!=null)
            return visitSimple_type(ctx.simple_type());
        return visitClass_type(ctx.class_type());
    }

    @Override
    public HashMap<String,String> visitSimple_type(CSharpParser.Simple_typeContext ctx) {
        HashMap<String,String> data = new HashMap<>();

        if(ctx.numeric_type() != null)
            data = visitNumeric_type(ctx.numeric_type());
    return data;
    }

    @Override
    public HashMap<String,String> visitNumeric_type(CSharpParser.Numeric_typeContext ctx) {
        HashMap<String,String> data = new HashMap<>();
        if(ctx.integral_type() != null)
            data = visitIntegral_type(ctx.integral_type());
        else if(ctx.floating_point_type() != null)
            data = visitFloating_point_type(ctx.floating_point_type());

        return data;
    }

    @Override
    public HashMap<String,String> visitIntegral_type(CSharpParser.Integral_typeContext ctx) {
        HashMap<String,String> data = new HashMap<>();

        if(ctx.INT().getText() != "")
            data.put("Xamarin_type",ctx.INT().getText());
        return data;
    }

    @Override
    public HashMap<String,String> visitFloating_point_type(CSharpParser.Floating_point_typeContext ctx) {
        HashMap<String,String> data = new HashMap<>();
        if(ctx.FLOAT() != null)
            data.put("Xamarin_type",ctx.FLOAT().getText());
        return data;
    }

    @Override
    public HashMap<String,String> visitClass_type(CSharpParser.Class_typeContext ctx) {
        HashMap<String,String> data = new HashMap<>();
        if(ctx.namespace_or_type_name() != null)
            data =  visitNamespace_or_type_name(ctx.namespace_or_type_name());

        if(ctx.STRING()!= null)
            data.put("Xamarin_type",ctx.STRING().getText());
        return data;
    }

    @Override
    public HashMap<String,String> visitNamespace_or_type_name(CSharpParser.Namespace_or_type_nameContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        HashMap<String,String> data = new HashMap<>();

        if(ctx.identifier().get(0).getText().equals("Dictionary")){
            XamrinToNativeAssistant.isMap = true;
            HashMap<String,String> mapDataType = visitType_argument_list(ctx.type_argument_list(0));
            data.put("Xamarin_type","Dictionary");
            data.put("key",mapDataType.get("key"));
            data.put("value",mapDataType.get("value"));
        }
        else if(ctx.identifier().get(0).getText().equals("ArrayList")){

            XamrinToNativeAssistant.isArrayList = true;
            data.put("Xamarin_type","ArrayList");


        }
        else
            data.put("Xamarin_type",ctx.getText());
        return data;
    }

    @Override
    public HashMap<String,String> visitType_argument_list(CSharpParser.Type_argument_listContext ctx) {
        HashMap<String,String> data = new HashMap<>();
        if(XamrinToNativeAssistant.isMap) {
            data.put("key",ctx.type_().get(0).base_type().getText());
            data.put("value",ctx.type_().get(1).base_type().getText());

        }

        return data;
    }

    @Override
    public ArrayList<String> visitLocal_variable_declarator(CSharpParser.Local_variable_declaratorContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();
        HashMap<String,String>data = new HashMap<>();
         if(XamrinToNativeAssistant.isArrayList){
             XamrinToNativeAssistant.arrayListName = ctx.identifier().getText();
            XamrinToNativeAssistant.addNewVariable(ctx.identifier().getText(),XamrinToNativeAssistant.localVariableTmpDatatype);

        }
        if(XamrinToNativeAssistant.isMap){
            XamrinToNativeAssistant.mapName = ctx.identifier().getText();
            output = visitLocal_variable_initializer(ctx.local_variable_initializer());
            XamrinToNativeAssistant.mapName = "";

        }

        else{
            javaMap.put("varName",ctx.identifier().getText());
            swiftMap.put("varName",ctx.identifier().getText());
            javaMap.put("data_type",mJavaAssistant.getDataType(XamrinToNativeAssistant.localVariableTmpDatatype));
            swiftMap.put("data_type",mSwiftAssistant.getDataType(XamrinToNativeAssistant.localVariableTmpDatatype));
            swiftMap.put("state","var");

            if(ctx.local_variable_initializer() != null) {
                temp = visitLocal_variable_initializer((CSharpParser.Local_variable_initializerContext) ctx.local_variable_initializer());
                javaMap.put("expression",temp.get(0));
                swiftMap.put("expression",temp.get(1));
            }

            if (XamrinToNativeAssistant.isArray){
                javaCode+=JavaObject.array_declaration(javaMap,temp.get(0));
                swiftCode+=SwiftObject.array_declaration(swiftMap,temp.get(1));
                String arrayDataTypeSign = "Array("+ XamrinToNativeAssistant.localVariableTmpDatatype+")";
                XamrinToNativeAssistant.addNewVariable(javaMap.get("varName"),arrayDataTypeSign);
                XamrinToNativeAssistant.isArray = false;
            }
            else {
                javaCode += JavaObject.variable_declaration(javaMap);
                swiftCode += SwiftObject.variable_declaration(swiftMap);
                XamrinToNativeAssistant.addNewVariable(javaMap.get("varName"), XamrinToNativeAssistant.localVariableTmpDatatype);
            }
            output.add(javaCode);
            output.add(swiftCode);

        }

        XamrinToNativeAssistant.localVariableTmpDatatype = "";
        return output;
    }

    @Override
    public ArrayList<String> visitLocal_variable_initializer(CSharpParser.Local_variable_initializerContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.array_initializer() != null)
            return visitArray_initializer(ctx.array_initializer());
        output = visitExpression(ctx.expression());
        return output;
    }

    @Override
    public ArrayList<String> visitArray_initializer(CSharpParser.Array_initializerContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        String txt = ctx.getText();
        txt = txt.substring(txt.indexOf("{")+1,txt.lastIndexOf("}"));
        output.add(txt);
        output.add(txt);

        return output;
    }

    /** End of Method Body Section **/
    /** End of Method Section **/
    @Override
    public ArrayList<String> visitTyped_member_declaration(CSharpParser.Typed_member_declarationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

//        String javaCode  = "";
//        String swiftCode = "";
//        HashMap<String,String> javaVariableDeclaration = new HashMap<>();
//        HashMap<String,String> swiftVariableDeclaration = new HashMap<>();
//        javaVariableDeclaration.put("data_type",mJavaAssistant.getDataType(ctx.type_().getText()));
//        swiftVariableDeclaration.put("data_type","Int"); // Later we will use Java Assistant when it is fixed


        if(ctx.method_declaration()!= null){
            XamrinToNativeAssistant.currentDataType = ctx.type_().getText();
            output = visitMethod_declaration(ctx.method_declaration());
        }else {

            saveVariableTypes(ctx.field_declaration().variable_declarators(), ctx.type_().getText());
            output = visitField_declaration(ctx.field_declaration());
        }
//        javaVariableDeclaration.put("varName",temp.get(0));
//        swiftVariableDeclaration.put("varName",temp.get(1));
//        swiftVariableDeclaration.put("state","var");
//        javaCode+=JavaObject.variable_declaration(javaVariableDeclaration);
//        swiftCode+=SwiftObject.variable_declaration(swiftVariableDeclaration);
       // output.add(javaCode);
       // output.add(swiftCode);

        return output;
    }
    private void saveVariableTypes(CSharpParser.Variable_declaratorsContext variableDeclaratorsContext, String type) {
        for (ParseTree p : variableDeclaratorsContext.variable_declarator()) {
            CSharpParser.Variable_declaratorContext temp = (CSharpParser.Variable_declaratorContext) p;

            XamrinToNativeAssistant.addNewAttribute(currentClass,temp.identifier().getText(), type);
        }
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
        javaCode+=JavaObject.newLine();
        swiftCode+=SwiftObject.newLine();
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitVariable_declarator(CSharpParser.Variable_declaratorContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode  = "";
        String swiftCode = "";

        String dataType = XamrinToNativeAssistant.getClassAttributeDataType(currentClass,ctx.identifier().getText());

        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();

        javaMap.put("varName",ctx.identifier().getText());
        swiftMap.put("varName",ctx.identifier().getText());
        javaMap.put("data_type",mJavaAssistant.getDataType(dataType));
        swiftMap.put("data_type",mSwiftAssistant.getDataType(dataType));
        swiftMap.put("state","var");
        if(ctx.variable_initializer() != null) {
           temp = visitVariable_initializer(ctx.variable_initializer());
           javaMap.put("expression",temp.get(0));
           swiftMap.put("expression",temp.get(1));
        }
        javaCode+=JavaObject.variable_declaration(javaMap);
        swiftCode+=SwiftObject.variable_declaration(swiftMap);
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitVariable_initializer(CSharpParser.Variable_initializerContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitExpression(ctx.expression());
        return output;
    }

    @Override
    public ArrayList<String> visitExpression(CSharpParser.ExpressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        if(ctx.assignment()!=null){
            output = visitAssignment(ctx.assignment());

        }else {
            output = visitNon_assignment_expression(ctx.non_assignment_expression());
        }

        return output;
    }

    @Override
    public ArrayList<String> visitAssignment(CSharpParser.AssignmentContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String,String> javaMap = new HashMap<>();
        HashMap<String,String> swiftMap = new HashMap<>();

        String javaCode = "";
        String swiftCode = "";
        String leftExpression = "";

        leftExpression = ctx.unary_expression().getText();
        temp = visitExpression(ctx.expression());

        javaMap.put("leftExpression",leftExpression);
        swiftMap.put("leftExpression",leftExpression);

        javaMap.put("rightExpression", temp.get(0));
        swiftMap.put("rightExpression", temp.get(1));


        javaCode+=JavaObject.assignment_expression(javaMap);
        swiftCode+=SwiftObject.assignment_expression(swiftMap);
        output.add(javaCode);
        output.add(swiftCode);

        return output;
    }

    @Override
    public ArrayList<String> visitNon_assignment_expression(CSharpParser.Non_assignment_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitConditional_expression(ctx.conditional_expression());
        return output;
    }

    @Override
    public ArrayList<String> visitConditional_expression(CSharpParser.Conditional_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitNull_coalescing_expression(ctx.null_coalescing_expression());
        return output;
    }

    @Override
    public ArrayList<String> visitNull_coalescing_expression(CSharpParser.Null_coalescing_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitConditional_or_expression(ctx.conditional_or_expression());
        return output;
    }

    @Override
    public ArrayList<String> visitConditional_or_expression(CSharpParser.Conditional_or_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output  = visitConditional_and_expression(ctx.conditional_and_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitConditional_and_expression(CSharpParser.Conditional_and_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitInclusive_or_expression(ctx.inclusive_or_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitInclusive_or_expression(CSharpParser.Inclusive_or_expressionContext ctx) {
       ArrayList<String> output = new ArrayList<>();
       ArrayList<String> temp = new ArrayList<>();
        output  = visitExclusive_or_expression(ctx.exclusive_or_expression(0));
       return output;
    }

    @Override
    public ArrayList<String> visitExclusive_or_expression(CSharpParser.Exclusive_or_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitAnd_expression(ctx.and_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitAnd_expression(CSharpParser.And_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitEquality_expression(ctx.equality_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitEquality_expression(CSharpParser.Equality_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        Queue<String> signs = new LinkedList<>();
        String javaCode = "";
        String swiftCode = "";

        for(ParseTree cc : ctx.children){
            if(cc.getClass().toString().contains("TerminalNodeImpl")){
                signs.add(cc.getText());
            }

        }
        for(CSharpParser.Relational_expressionContext c : ctx.relational_expression()) {
            //output = visitShift_expression(ctx.shift_expression(0));
            temp = visitRelational_expression(c);

            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
            if(signs.size()>0) {
                String sign = signs.remove();
                javaCode += sign;
                swiftCode += sign;
            }
            javaCode+=" "; // useless step
            swiftCode+=" "; // useless step
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitRelational_expression(CSharpParser.Relational_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
       String javaCode = "";
       String swiftCode = "";
       Queue<String> signs = new LinkedList<>();
        for(ParseTree cc : ctx.children){
                if(cc.getClass().toString().contains("TerminalNodeImpl")){
                    signs.add(cc.getText());
                }

        }

        for(CSharpParser.Shift_expressionContext c : ctx.shift_expression()) {
            //output = visitShift_expression(ctx.shift_expression(0));
            temp = visitShift_expression(c);

            javaCode+=temp.get(0);
            swiftCode+=temp.get(1);
            if(signs.size()>0) {
                String sign = signs.remove();
                javaCode += sign;
                swiftCode += sign;
            }
            javaCode+=" "; // useless step
            swiftCode+=" "; // useless step
        }
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitShift_expression(CSharpParser.Shift_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitAdditive_expression(ctx.additive_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitAdditive_expression(CSharpParser.Additive_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode  = "";
        String swiftCode = "";
        if(ctx.multiplicative_expression(0)!=null){
            for(CSharpParser.Multiplicative_expressionContext i  : ctx.multiplicative_expression()){
                temp = visitMultiplicative_expression(i);
                javaCode += temp.get(0);
                swiftCode+=temp.get(1);

            }

            output.add(javaCode);
            output.add(swiftCode);
            return output;

        }
        HashMap<String,String> javaAdditiveExpression= new HashMap<>();
        HashMap<String,String> swiftAdditiveExpression= new HashMap<>();
        javaAdditiveExpression.put("operator","+");
        swiftAdditiveExpression.put("operator","+");
        temp = (ArrayList<String>) visit(ctx.getChild(0));
        javaAdditiveExpression.put("leftExpression",temp.get(0));
        swiftAdditiveExpression.put("leftExpression",temp.get(1));
        temp = (ArrayList<String>) visit(ctx.getChild(2));
        javaAdditiveExpression.put("rightExpression",temp.get(0));
        swiftAdditiveExpression.put("rightExpression",temp.get(1));
        javaCode+=JavaObject.expression_operator_expression(javaAdditiveExpression);
        swiftCode+=SwiftObject.expression_operator_expression(swiftAdditiveExpression);
        //temp = visitMultiplicative_expression((CSharpParser.Multiplicative_expressionContext) ctx.multiplicative_expression());
       // javaAdditiveExpression.put("leftExpression","");
        output.add(javaCode);
        output.add(swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitMultiplicative_expression(CSharpParser.Multiplicative_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        if(ctx.switch_expression(0)!=null){
            for(CSharpParser.Switch_expressionContext i  : ctx.switch_expression()){
                temp = visitSwitch_expression(i);
                javaCode += temp.get(0);
                swiftCode+=temp.get(1);

            }


        }
       // output = visitSwitch_expression(ctx.switch_expression(0));
        output.add(javaCode);
        output.add(swiftCode);
;        return output;
    }

    @Override
    public ArrayList<String> visitSwitch_expression(CSharpParser.Switch_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitRange_expression(ctx.range_expression());
        return output;
    }

    @Override
    public ArrayList<String> visitRange_expression(CSharpParser.Range_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        if(ctx.unary_expression(0)!=null){
            for(CSharpParser.Unary_expressionContext i  : ctx.unary_expression()){
                temp = visitUnary_expression(i);
                javaCode += temp.get(0);
                swiftCode+=temp.get(1);

            }


        }
        output.add(javaCode);
        output.add(swiftCode);
        //output = visitUnary_expression(ctx.unary_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitUnary_expression(CSharpParser.Unary_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();

        if(patternsMatcher.CheckRealmLogoutStatementPattern(ctx.getText()))
        {

            output = patternsMatcher.RealmLogoutStatementPattern(ctx.getText());
            return output;
        }
        output = visitPrimary_expression(ctx.primary_expression());
        return output;
    }

    public ArrayList<String> Recode(CSharpParser.Primary_expressionContext ctx){
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String,String> javaMultipleFunction= new HashMap<>();  // useless
        HashMap<String,String> swiftMultipleFunction= new HashMap<>(); // useless
        String javaOutput = "";
        String swiftOuput = "";
        String startName = "";

        if(ctx.primary_expression_start()!=null){
            startName = ctx.primary_expression_start().getText();
            javaMultipleFunction.put("Caller",startName); // useless
            swiftMultipleFunction.put("Caller",startName); // useless
            if(ctx.member_access(0)==null){
                // Argument like collection("users") and we want "users"
                ArrayList<String> argument = new ArrayList<>();
                argument.add(startName);
                argument.add(startName);
                return argument;
            }

            if(!XamrinToNativeAssistant.isVariableFoundInCurrentMethod(startName)){
                // Static Function like FirebaseDB.Create();
                javaOutput+=mJavaAssistant.getDataType(startName);
                swiftOuput+=mSwiftAssistant.getDataType(startName);
                javaOutput+=".";
                swiftOuput+=".";


                String functionName = visitMember_access(ctx.member_access(0));
                String argumentDataType = "";

                if(ctx.method_invocation(0)!=null)
                    temp = visitMethod_invocation(ctx.method_invocation(0));
                if(!temp.isEmpty()) {
                    argumentDataType = XamrinToNativeAssistant.checkDataTypeOfVariable((temp.get(0)));
                }

                String functionDefinition = functionName + "(" + argumentDataType + ")";
                javaOutput+=mJavaAssistant.getStaticFunction(startName,functionDefinition);
                swiftOuput+=mSwiftAssistant.getStaticFunction(startName,functionDefinition);

                javaOutput+=";\n";
                swiftOuput+=";\n";

            }
            else{
                    if(ctx.member_access(0)!=null){
                        // Multiple Function like db.Collection("users").Document("ali");
                        int counter = 0;
                        String argumentDataType = "";
                        String rootObjectDatatype = XamrinToNativeAssistant.getCurrentMethodVariableDataType(startName);
                        javaOutput+=startName;
                        swiftOuput+=startName;
                        javaOutput+=".";
                        swiftOuput+=".";
                        //String rootJavaType = mJavaAssistant.getDataType(rootObjectDatatype);
                        //String rootSwiftType = mSwiftAssistant.getDataType(rootJavaType);
                        mJavaAssistant.lastFunctionReturnType = mJavaAssistant.getDataType(rootObjectDatatype);
                        mSwiftAssistant.lastFunctionReturnType = mSwiftAssistant.getDataType(rootObjectDatatype);

                        for(CSharpParser.Member_accessContext i : ctx.member_access()){
                            String functionName = visitMember_access(ctx.member_access(counter));

                            if(ctx.method_invocation(counter)!= null) {

                                temp = visitMethod_invocation(ctx.method_invocation(counter));
                                String[] sourceParameters;
                                if(!temp.isEmpty()) {
                                    sourceParameters = temp.get(0).split(", *");
                                    for(int i2 = 0;i2<sourceParameters.length;i2++){
                                        argumentDataType += XamrinToNativeAssistant.checkDataTypeOfVariable(sourceParameters[i2]);
                                        argumentDataType+=",";
                                    }
                                    argumentDataType = argumentDataType.substring(0,argumentDataType.lastIndexOf(","));
                                }

                                String functionDefinition = functionName + "(" + argumentDataType + ")";
                                String functionInJava = mJavaAssistant.getFunctionInJava(mJavaAssistant.lastFunctionReturnType,functionDefinition);
                                String functionInSwift = mSwiftAssistant.getFunctionInSwift(mSwiftAssistant.lastFunctionReturnType,functionDefinition);
                                String parametersInXamarin = ctx.method_invocation(counter).getText();
                                parametersInXamarin = parametersInXamarin.substring(parametersInXamarin.indexOf("(")+1,parametersInXamarin.lastIndexOf(")"));
                                String[] parametersArray = parametersInXamarin.split(", *");

                                for(int j = 0;j<parametersArray.length;j++){
                                    if(!functionInJava.contains("/"))
                                        break;
                                    String argumentInJava = temp.get(0);
                                    String argumentInSwift = temp.get(1);
                                    functionInJava = functionInJava.replace('/' + String.valueOf(j), argumentInJava);
                                    functionInSwift = functionInSwift.replace('/' + String.valueOf(j), argumentInSwift);
                                }
                                javaOutput+=functionInJava;
                                swiftOuput+=functionInSwift;

                                mJavaAssistant.lastFunctionReturnType = mJavaAssistant.getFunctionReturnType(mJavaAssistant.lastFunctionReturnType, functionDefinition);

                                mSwiftAssistant.lastFunctionReturnType = mSwiftAssistant.getFunctionReturnType(mSwiftAssistant.lastFunctionReturnType, functionDefinition);

                                javaOutput+=".";
                                swiftOuput+=".";
                            }

                            counter++;
                        }
                        javaOutput = javaOutput.substring(0,javaOutput.lastIndexOf("."));
                        swiftOuput = swiftOuput.substring(0,swiftOuput.lastIndexOf("."));
                        javaOutput+=";\n";
                        swiftOuput+=";\n";

                        mJavaAssistant.lastFunctionReturnType = "";
                        mSwiftAssistant.lastFunctionReturnType = "";
                    }
                    else{

                        ArrayList<String> argument = new ArrayList<>();
                        argument.add(startName);
                        argument.add(startName);
                        return argument;
                    }

            }
        }
        javaMultipleFunction.put("Block",javaOutput); // useless
        swiftMultipleFunction.put("Block",swiftOuput); // useless

        output.add(javaOutput);
        output.add(swiftOuput);
        return output;
    }

    @Override
    public ArrayList<String> visitPrimary_expression(CSharpParser.Primary_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String,String> javaMultipleFunction= new HashMap<>();  // useless
        HashMap<String,String> swiftMultipleFunction= new HashMap<>(); // useless
        String javaOutput = "";
        String swiftOuput = "";
        String startName = "";




        if(patternsMatcher.CheckRealmWriteForDeleteStatementPattern(ctx.getText())){

          output = patternsMatcher.RealmWriteForDeleteStatementPattern(ctx.getText());
          return  output;
        }
        if(patternsMatcher.CheckRealmWriteForUpdateStatementPattern(ctx.getText())) {
            output = patternsMatcher.RealmWriteForUpdateStatementPattern(ctx.getText());
            return output;
        }
        if(patternsMatcher.RealmWriteStatementPatternCheck(ctx.getText())) {

            output = patternsMatcher.RealmWriteStatementPattern(ctx.getText());
            return output;
        }

        // Dictionary Conversion
        if(XamrinToNativeAssistant.isMap){
            if(ctx.primary_expression_start() != null){

                HashMap<String,String> data = new HashMap<>();
                data = visitPrimary_Expression_Start(ctx.primary_expression_start());
                HashMap<String,String> javaMapData = new HashMap<>();
                HashMap<String,String> swiftMapData = new HashMap<>();

                javaMapData.put("mapName",XamrinToNativeAssistant.mapName);
                javaMapData.put("key",mJavaAssistant.getDataType(data.get("key")));
                javaMapData.put("value",mJavaAssistant.getDataType(data.get("value")));
                javaMapData.put("type",mJavaAssistant.getDataType(data.get("Xamarin_type")));
                swiftMapData.put("mapName",XamrinToNativeAssistant.mapName);
                swiftMapData.put("key",mSwiftAssistant.getDataType(data.get("key")));
                swiftMapData.put("value",mSwiftAssistant.getDataType(data.get("value")));
                javaOutput+=JavaObject.map_declaration(javaMapData);
                swiftOuput+=SwiftObject.map_declaration(swiftMapData);
                output.add(javaOutput);
                output.add(swiftOuput);
                XamrinToNativeAssistant.isMap = false;
                String dataTypeInXamarin = data.get("Xamarin_type") + "<" + data.get("key") + "," + data.get("value") + ">";
                XamrinToNativeAssistant.addNewVariable(XamrinToNativeAssistant.mapName,dataTypeInXamarin);
                return output;
            }
        }
        if(XamrinToNativeAssistant.isArrayList){
            if(ctx.primary_expression_start() != null){
                javaOutput+="new ArrayList()";
                swiftOuput+="[AnyObject]()\n";
                HashMap<String,String> data = new HashMap<>();

                data = visitPrimary_Expression_Start(ctx.primary_expression_start());
                javaOutput+=data.get("Java");
                swiftOuput+=data.get("Swift");
                XamrinToNativeAssistant.isArrayList = false;
                output.add(javaOutput);
                output.add(swiftOuput);
                return output;
            }

        }
        // Bracket Expression Conversion like book["name"]
        if(ctx.bracket_expression()!=null){
            visitBracket_expression(ctx.bracket_expression(0));
            for(CSharpParser.Bracket_expressionContext i : ctx.bracket_expression()){

                visitBracket_expression(i);
            }
        }
        if(ctx.primary_expression_start()!=null){
            startName = ctx.primary_expression_start().getText();

            // statement like x++
            if(ctx.OP_INC().size()!= 0){
                javaOutput+=(startName+ctx.OP_INC(0));
                swiftOuput+=(startName+ctx.OP_INC(0));
                output.add(javaOutput);
                output.add(swiftOuput);

                return output;
            }
            javaMultipleFunction.put("Caller",startName); // useless
            swiftMultipleFunction.put("Caller",startName); // useless
            if(ctx.member_access(0)==null){
                // Argument like collection("users") and we want "users"
                ArrayList<String> argument = new ArrayList<>();
                argument.add(startName);
                argument.add(startName);
                return argument;
            }

            if(!XamrinToNativeAssistant.isVariableFoundInCurrentMethod(startName)){
                // Static Function like FirebaseDB.Create();
                javaOutput+=mJavaAssistant.getDataType(startName);
                swiftOuput+=mSwiftAssistant.getDataType(startName);
                javaOutput+=".";
                swiftOuput+=".";


                String functionName = visitMember_access(ctx.member_access(0));
                String argumentDataType = "";
                if(ctx.method_invocation(0)!=null)
                    temp = visitMethod_invocation(ctx.method_invocation(0));
                if(!temp.isEmpty()) {
                    argumentDataType = XamrinToNativeAssistant.checkDataTypeOfVariable((temp.get(0)));
                }
                String functionDefinition = functionName + "(" + argumentDataType + ")";
                javaOutput+=mJavaAssistant.getStaticFunction(startName,functionDefinition);
                swiftOuput+=mSwiftAssistant.getStaticFunction(startName,functionDefinition);

                javaOutput+=";\n";
                swiftOuput+=";\n";

            }
            else{
                if(ctx.member_access(0)!=null){
                    // Multiple Function like db.Collection("users").Document("ali");
                    int counter = 0;
                    String argumentDataType = "";
                    String rootObjectDatatype = XamrinToNativeAssistant.getCurrentMethodVariableDataType(startName);
                    javaOutput+=startName;
                    swiftOuput+=startName;
                    javaOutput+=".";
                    swiftOuput+=".";
                    //String rootJavaType = mJavaAssistant.getDataType(rootObjectDatatype);
                    //String rootSwiftType = mSwiftAssistant.getDataType(rootJavaType);

                    mJavaAssistant.lastFunctionReturnType = mJavaAssistant.getDataType(rootObjectDatatype);
                    mSwiftAssistant.lastFunctionReturnType = mSwiftAssistant.getDataType(rootObjectDatatype);

                    for(CSharpParser.Member_accessContext i : ctx.member_access()){
                        String functionName = visitMember_access(ctx.member_access(counter));
                        String[] javaParameters;
                        String[] swiftParameters;
                        if(ctx.method_invocation(counter)!= null) {
                            temp = visitMethod_invocation(ctx.method_invocation(counter));

                            String[] sourceParameters;
                            if(!temp.isEmpty()) {

                                sourceParameters = temp.get(0).split(", *");
                                for(int i2 = 0;i2<sourceParameters.length;i2++){
                                    argumentDataType += XamrinToNativeAssistant.checkDataTypeOfVariable(sourceParameters[i2]);
                                    argumentDataType+=",";
                                }
                                argumentDataType = argumentDataType.substring(0,argumentDataType.lastIndexOf(","));
                            }

                            String functionDefinition = functionName + "(" + argumentDataType + ")";

                            String functionInJava = mJavaAssistant.getFunctionInJava(mJavaAssistant.lastFunctionReturnType,functionDefinition);

                            String functionInSwift = mSwiftAssistant.getFunctionInSwift(mSwiftAssistant.lastFunctionReturnType,functionDefinition);
                            String parametersInXamarin = ctx.method_invocation(counter).getText();

                            boolean emptyParameters = false;
                            if(parametersInXamarin.length() == 2){
                                emptyParameters = true;
                            }
                            parametersInXamarin = parametersInXamarin.substring(parametersInXamarin.indexOf("(")+1,parametersInXamarin.lastIndexOf(")"));

                            String[] parametersArray = parametersInXamarin.split(", *");

                            for(int j = 0;j<parametersArray.length;j++) {
                                if (!functionInJava.contains("/"))
                                    break;
                                if (emptyParameters) {
                                    functionInJava = functionInJava.replace('/' + String.valueOf(j), "");
                                    functionInSwift = functionInSwift.replace('/' + String.valueOf(j), "");
                                } else {
                                    javaParameters = temp.get(0).split(", *");
                                    swiftParameters = temp.get(1).split(", *");
                                    String argumentInJava = javaParameters[j];
                                    String argumentInSwift = swiftParameters[j];
                                    functionInJava = functionInJava.replace('/' + String.valueOf(j), argumentInJava);
                                    functionInSwift = functionInSwift.replace('/' + String.valueOf(j), argumentInSwift);
                                }
                            }
                            javaOutput+=functionInJava;
                            swiftOuput+=functionInSwift;

                            mJavaAssistant.lastFunctionReturnType = mJavaAssistant.getFunctionReturnType(mJavaAssistant.lastFunctionReturnType, functionDefinition);

                            mSwiftAssistant.lastFunctionReturnType = mSwiftAssistant.getFunctionReturnType(mSwiftAssistant.lastFunctionReturnType, functionDefinition);

                            javaOutput+=".";
                            swiftOuput+=".";
                        }
                        argumentDataType = "";
                        counter++;
                    }
                    javaOutput = javaOutput.substring(0,javaOutput.lastIndexOf("."));
                    swiftOuput = swiftOuput.substring(0,swiftOuput.lastIndexOf("."));
                    javaOutput+=";\n";
                    swiftOuput+=";\n";

                    mJavaAssistant.lastFunctionReturnType = "";
                    mSwiftAssistant.lastFunctionReturnType = "";
                }
                else{

                    ArrayList<String> argument = new ArrayList<>();
                    argument.add(startName);
                    argument.add(startName);
                    return argument;
                }

            }
        }
        javaMultipleFunction.put("Block",javaOutput); // useless
        swiftMultipleFunction.put("Block",swiftOuput); // useless

        output.add(javaOutput);
        output.add(swiftOuput);
        return output;
    }

    @Override
    public ArrayList<String> visitBracket_expression(CSharpParser.Bracket_expressionContext ctx) {

        String javaOutput = "";
        String swiftOutput = "";
        ArrayList<String> temp = new ArrayList<>();

        //temp.add(ctx.getText());
        //temp.add(ctx.getText());
        //visitIndexer_argument(ctx.indexer_argument(0));


        return temp;
    }

    @Override
    public ArrayList<String> visitIndexer_argument(CSharpParser.Indexer_argumentContext ctx) {
        ArrayList<String>output = new ArrayList<>();

        output = visitExpression(ctx.expression());


        return output;
    }

    public HashMap<String,String> visitPrimary_Expression_Start(CSharpParser.Primary_expression_startContext ctx){
        HashMap<String,String> data = new HashMap<>();
        // to handle statement like ArrayList(){1,2,3}
        if(XamrinToNativeAssistant.isArrayList) {
            if (ctx.getChildCount() > 2){

                if(ctx.getChild(2).getClass().toString().contains("Object_creation_expressionContext")){
                        data = visitObject_creation_expression((CSharpParser.Object_creation_expressionContext) ctx.getChild(2));
                        return data;
                }
            }
        }
        if(ctx.getChild(1) != null)
            return visitType_((CSharpParser.Type_Context) ctx.getChild(1));

        return data;
    }

    @Override
    public HashMap<String,String> visitObject_creation_expression(CSharpParser.Object_creation_expressionContext ctx) {
        HashMap<String,String> output = new HashMap<>();
        if(ctx.object_or_collection_initializer() != null){
            output = visitObject_or_collection_initializer(ctx.object_or_collection_initializer());
        }
        return output;
    }

    @Override
    public HashMap<String,String> visitObject_or_collection_initializer(CSharpParser.Object_or_collection_initializerContext ctx) {
        HashMap<String,String> output = new HashMap<>();
        if(ctx.collection_initializer() != null){
                output = visitCollection_initializer(ctx.collection_initializer());
        }
        return output;
    }

    @Override
    public HashMap<String,String> visitCollection_initializer(CSharpParser.Collection_initializerContext ctx) {
        HashMap<String,String> output = new HashMap<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaCode = "";
        String swiftCode = "";
        HashMap<String,String> swiftMap = new HashMap<>();
        if(XamrinToNativeAssistant.isArrayList){
            XamrinToNativeAssistant.isArrayList = false;
            javaCode+="{";
            swiftMap.put("name",XamrinToNativeAssistant.arrayListName);
            for(CSharpParser.Element_initializerContext c : ctx.element_initializer()){
                if(c.non_assignment_expression() != null){
                    temp = visitNon_assignment_expression(c.non_assignment_expression());
                    swiftMap.put("element",temp.get(1));
                    javaCode+=temp.get(0);
                    javaCode+=",";
                    swiftCode+=SwiftObject.append(swiftMap);
                }
            }
            if(javaCode.endsWith(","))
                javaCode = javaCode.substring(0,javaCode.lastIndexOf(","));
            javaCode+="}";

        }

        output.put("Java",javaCode);
        output.put("Swift",swiftCode);
        return output;
    }

    @Override
    public ArrayList<String> visitElement_initializer(CSharpParser.Element_initializerContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.non_assignment_expression() != null){
            output = visitNon_assignment_expression(ctx.non_assignment_expression());
        }
        return output;
    }

    @Override
    public String visitMember_access(CSharpParser.Member_accessContext ctx) {

        return ctx.identifier().getText().substring(0,ctx.identifier().getText().length());
    }

    @Override
    public ArrayList<String> visitMethod_invocation(CSharpParser.Method_invocationContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        if(ctx.argument_list() != null)
            output = visitArgument_list(ctx.argument_list());
        return output;
    }

    @Override
    public ArrayList<String> visitArgument_list(CSharpParser.Argument_listContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String javaOutput = "";
        String swiftOutput = "";
        if(ctx.argument(0)!=null) {
            for(CSharpParser.ArgumentContext i : ctx.argument()){
                    temp = visitArgument(i);
                    javaOutput+=temp.get(0);
                    swiftOutput+=temp.get(1);
                    javaOutput+=",";
                    swiftOutput+=",";
            }
            javaOutput = javaOutput.substring(0,javaOutput.lastIndexOf(","));
            swiftOutput = swiftOutput.substring(0,swiftOutput.lastIndexOf(","));
            output.add(javaOutput);
            output.add(swiftOutput);
            return output;
        }
        output.add("");
        output.add("");
        return output;
    }

    @Override
    public ArrayList<String> visitArgument(CSharpParser.ArgumentContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        output = visitExpression(ctx.expression());
        return output;
    }

    @Override
    public Object visitDeclarationStatement(CSharpParser.DeclarationStatementContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        //output.add();
        //output.add();
        visitLocal_variable_declaration(ctx.local_variable_declaration());
        return super.visitDeclarationStatement(ctx);
    }

}