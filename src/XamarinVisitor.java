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
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLOutput;
import java.util.*;

/** There is a problem in Java Assistant  Class **/


public  class XamarinVisitor extends CSharpParserBaseVisitor {
    public static String currentClass;
    public static String currentMethod;

    Java JavaObject;
    Swift SwiftObject;
    JavaAssistant mJavaAssistant;

    public XamarinVisitor(){
        JavaObject  = new Java();
        SwiftObject = new Swift();
        mJavaAssistant = new JavaAssistant();
    }




    @Override
    public ArrayList<String> visitCompilation_unit(CSharpParser.Compilation_unitContext ctx) {
        ArrayList<String> output = visitNamespace_member_declarations(ctx.namespace_member_declarations());
        System.out.println(" Java Code " + output.get(0));
        System.out.println(" Swift Code " + output.get(1));
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
        swiftClassDeclaration.put("className",ctx.identifier().getText().toString());
        swiftClassDeclaration.put("accessModifier","");
        //classDeclaration.put("inherit","");
        swiftCode+=SwiftObject.class_declaration(swiftClassDeclaration);
        javaCode+=JavaObject.class_declaration(javaClassDeclaration);
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
//        String javaCode  = "";
//        String swiftCode = "";
//        HashMap<String,String> javaVariableDeclaration = new HashMap<>();
//        HashMap<String,String> swiftVariableDeclaration = new HashMap<>();
//        javaVariableDeclaration.put("data_type",mJavaAssistant.getDataType(ctx.type_().getText()));
//        swiftVariableDeclaration.put("data_type","Int"); // Later we will use Java Assistant when it is fixed
        saveVariableTypes(ctx.field_declaration().variable_declarators(),ctx.type_().getText());
        output = visitField_declaration(ctx.field_declaration());
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
        swiftMap.put("data_type",mJavaAssistant.getDataType(dataType));
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
        output = visitNon_assignment_expression(ctx.non_assignment_expression());
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
        output = visitRelational_expression(ctx.relational_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitRelational_expression(CSharpParser.Relational_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitShift_expression(ctx.shift_expression(0));
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
        output = visitSwitch_expression(ctx.switch_expression(0));
        return output;
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
        output = visitUnary_expression(ctx.unary_expression(0));
        return output;
    }

    @Override
    public ArrayList<String> visitUnary_expression(CSharpParser.Unary_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output = visitPrimary_expression(ctx.primary_expression());
        return output;
    }

    @Override
    public ArrayList<String> visitPrimary_expression(CSharpParser.Primary_expressionContext ctx) {
        ArrayList<String> output = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        output.add(ctx.getText());
        output.add(ctx.getText());
        //output.add(ctx.identifier());
        return output;
    }
}