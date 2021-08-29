package com.project.phase1CodeGeneration;

import java.io.*;
import java.util.Vector;

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
    private static final String doubleQuotation = "\"";
    private static final String underscore = "_";

    private static final String newLine = "\n";
    private static final String tab = "\t";
    private static final String lineComment = "//";
    private static final String whiteSpace = " ";

    private static final String sharp = "#";
    private static final String include = "include";
    private static final String define = "define";
    private static final String ifndef = "ifndef";
    private static final String endif = "endif";

    private static final String struct = "struct";
    private static final String union = "union";

    public Phase1FileGenerator(CompleteDiagram diagram)
    {
        successFull = false;
        if(diagram.isSuccessCode())
        {
            try {
                File f = new File("phase1");
                f.mkdir();
                f = new File("headers");
                f.mkdir();

                OutputStream headersOutputStream = new FileOutputStream("headers/AllClasses.h");
                headersOutputStream.write(
                        generateAllClassesHeaderFile(diagram.allClassNames(),"AllClasses.h").getBytes());
                headersOutputStream.flush();
                headersOutputStream.close();
                for(CompleteClass completeClass:diagram.getClasses())
                {
                    OutputStream CPPFileOutputStream =
                            new FileOutputStream("phase1/" + completeClass.getName() + "Class.cpp");
                    CPPFileOutputStream.write(generateClassCPP(completeClass).getBytes());
                    CPPFileOutputStream.flush();
                    CPPFileOutputStream.close();

                    OutputStream headerFileOutputStream =
                            new FileOutputStream("headers/" + generateHeaderName(completeClass.getName()));
                    headerFileOutputStream.write(generateClassHeaderFile(completeClass).getBytes());
                    headerFileOutputStream.flush();
                    headerFileOutputStream.close();

                }
                successFull = true;
            } catch (IOException e) {
                e.printStackTrace();
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
                + EmptyBlock;      // TODO use @StringBuilder
    }

    private String generateConstructor(String className, CompleteConstructor constructor)
    {
        String baseClassName = className + doubleColon + className;
        return baseClassName + constructor.getShowName() + EmptyBlock;      // TODO use @StringBuilder
    }

    private String generateDestructor(String className)
    {
        String baseClassName = className + destruct + className;
        return baseClassName + EmptyBlock;
    }

    private String generateHeaderName(String className)
    {
        return className + "Class.h";
    }

    private String generateClassHeaderFile(CompleteClass completeClass)
    {
        StringBuilder allLines = new StringBuilder();
        String fileName = generateHeaderName(completeClass.getName());
        allLines.append(generateIncludeGuard(fileName));
        allLines.append(newLine);
        allLines.append(generateIncludeClassHeader("AllClasses.h"));//TODO replace AllClasses.h with a PSVSf
        allLines.append(newLine);
        allLines.append(generateIncludesForAClass(completeClass.getParents()));
        allLines.append(newLine);
        allLines.append(newLine);

        allLines.append(generateUnionDefinitions(completeClass));
        allLines.append(newLine);
        allLines.append(generateMethodDefinitions(completeClass.getAllMethods(), completeClass.getName()));
        //TODO add constructors
        allLines.append(newLine);

        allLines.append(generateEndGuard());
        return allLines.toString();
    }

    private String generateIncludesForAClass(Vector<String> classes)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classes)
            allLines.append(generateIncludeClassHeader(generateHeaderName(className)));
        return allLines.toString();
    }

    private String generateUnionDefinitions(CompleteClass completeClass)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(union + whiteSpace + completeClass.getName() + newLine + openCurlyBracket);
        allLines.append(generateUnionUsage(completeClass.getParents()));// TODO: except itself

        allLines.append(tab + struct + newLine);
        allLines.append(tab + openCurlyBracket + newLine);
        for(CompleteAttribute attribute:completeClass.getAllAttributes())
            allLines.append(tab + tab + attribute.getShowName() + semiColon + newLine);
        allLines.append(tab + closeCurlyBracket);
        allLines.append(newLine);

        allLines.append(closeCurlyBracket);
        allLines.append(newLine);
        return allLines.toString();
    }

    private String generateMethodDefinitions(Vector<CompleteMethod> methods, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(CompleteMethod method:methods)
            allLines.append(method.getShowName().replace(openParenthesis,//TODO :change replace rule for param size = 0
                    openParenthesis + union + whiteSpace +  className + whiteSpace + comma)).append(newLine);
        return allLines.toString();
    }

    private String generateAllClassesHeaderFile(Vector<String> classes, String fileName)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateIncludeGuard(fileName));
        allLines.append(newLine);
        allLines.append(generateInlineDefinitionOfClass(classes));
        allLines.append(newLine);
        allLines.append(generateEndGuard());
        return allLines.toString();
    }

    private String generateOneInlineDefinitionOfClass(String className)
    {
        return union + whiteSpace + className + semiColon;
    }

    private String generateInlineDefinitionOfClass(Vector<String> classNames)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classNames)
            allLines.append(generateOneInlineDefinitionOfClass(className)).append(newLine);

        return allLines.toString();
    }

    private String generateOneUnionUsage(String className)
    {
        return tab + union + whiteSpace + className + whiteSpace + union + className + semiColon + newLine;
    }

    private String generateUnionUsage(Vector<String> classNames)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classNames)
            allLines.append(generateOneUnionUsage(className));

        return allLines.toString();
    }

    private String generateIncludeGuard(String headerFile)
    {
        String key = underscore + headerFile.toUpperCase().replace(Dot,underscore) + underscore;
        return sharp + ifndef + whiteSpace + key + newLine +
                sharp + define + whiteSpace + key + newLine;
    }

    private String generateEndGuard()
    {
        return sharp + endif + newLine;
    }

    private String generateIncludeClassHeader(String headerFile)
    {
        return sharp + include + whiteSpace + doubleQuotation + headerFile + doubleQuotation + newLine;
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
