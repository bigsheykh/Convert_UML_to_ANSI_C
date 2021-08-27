package com.project.phase1CodeGeneration;

import java.io.*;

public class Phase1FileGenerator {

    boolean successFull;
    private static final String openParenthesis = "(";
    private static final String closeParenthesis = ")";
    private static final String openCurlyBracket = "{";
    private static final String closeCurlyBracket = "}";
    private static final String openSquareBracket = "[";
    private static final String closeSquareBracket = "]";

    private static final String ellipsis = "...";
    private static final String star = "*";
    private static final String arrow = "->";
    private static final String Dot = ".";
    private static final String comma = ",";
    private static final String semiColon = ";";
    private static final String colon = ":";
    private static final String tilde = "~";
    private static final String doubleColon = "::";
    private static final String destruct = "::~";

    private static final String newLine = "\n";
    private static final String tab = "\t";
    private static final String lineComment = "//";
    private static final String whiteSpace = " ";

    public Phase1FileGenerator(CompleteDiagram diagram)
    {
        successFull = false;
        if(diagram.isSuccessCode())
        {
            try {
                File f = new File("phase1");
                f.mkdir();

                for(CompleteClass completeClass:diagram.getClasses())
                {
                        OutputStream fileOutputStream =
                                new FileOutputStream("phase1/" + completeClass.getName() + ".cpp");
                        fileOutputStream.write(generateClassCPP(completeClass).getBytes());
                }
                successFull = true;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public boolean isSuccessFull() {
        return successFull;
    }

    private static final String EmptyBlock = newLine + openCurlyBracket + newLine +
            tab + lineComment + whiteSpace + "TODO:code here" + newLine +
            closeCurlyBracket + newLine + newLine;

    private String generateMethod(String className, CompleteMethod method)
    {
        String baseClassName = method.getReturnValueType().getShowName() + whiteSpace + className + doubleColon;
        return baseClassName +
                method.getShowName().substring(method.getReturnValueType().getShowName().length() + 1)
                + EmptyBlock;
    }

    private String generateConstructor(String className, CompleteConstructor constructor)
    {
        String baseClassName = className + doubleColon + className;
        return baseClassName + constructor.getShowName() + EmptyBlock;
    }

    private String generateDestructor(String className)
    {
        String baseClassName = className + destruct + className;
        return baseClassName + EmptyBlock;
    }

    private String generateClassCPP(CompleteClass completeClass)
    {
        StringBuilder base = new StringBuilder();
        for(CompleteConstructor constructor:completeClass.getConstructors())
            base.append(generateConstructor(completeClass.getName(), constructor));
        for(CompleteMethod method:completeClass.getMethods())
            base.append(generateMethod(completeClass.getName(), method));
        if(completeClass.isHavingDestructor())
            base.append(generateDestructor(completeClass.getName()));
        return base.toString();
    }

}
