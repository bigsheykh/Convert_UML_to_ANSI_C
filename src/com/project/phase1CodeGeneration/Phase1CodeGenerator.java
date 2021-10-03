package com.project.phase1CodeGeneration;

import com.project.classBaseUML.DescriptiveMember;
import org.javatuples.Pair;

import java.io.*;
import java.util.Vector;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

///TODO delete this and move all functions into Complete* classes
public class Phase1CodeGenerator {

    boolean successFull;

    /// TODO move to CompleteDiagram
    public Phase1CodeGenerator(CompleteDiagram diagram)
    {
        successFull = false;
        if(diagram.isSuccessCode())
        {
            try {
                File f = new File("phase1");
                f.mkdir();
                f = new File("headers");
                f.mkdir();
                f = new File("diagram_info");
                f.mkdir();

                OutputStream headersOutputStream = new FileOutputStream("headers/AllClasses.h");
                headersOutputStream.write(
                        generateAllClassesHeaderFile(diagram.allClassNames(),"AllClasses.h").getBytes());
                headersOutputStream.flush();
                headersOutputStream.close();
                OutputStream allInfoOutputStream = new FileOutputStream("diagram_info/AllClasses.info");
                allInfoOutputStream.write(diagram.generateClassNamesSeparatedByNewline().getBytes());
                allInfoOutputStream.flush();
                allInfoOutputStream.close();

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

                    OutputStream CFileOutputStream =
                            new FileOutputStream("headers/c_" + completeClass.getName() + ".c");
                    CFileOutputStream.write(generateClassC(completeClass, diagram.allClassNames()).getBytes());
                    CFileOutputStream.flush();
                    CFileOutputStream.close();

                    OutputStream infoFileOutputStream =
                            new FileOutputStream("diagram_info/" + completeClass.getName() + ".info");
                    infoFileOutputStream.write(completeClass.getPhase2Info().getBytes());
                    infoFileOutputStream.flush();
                    infoFileOutputStream.close();

                }
                successFull = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateClassC(CompleteClass completeClass, Vector<String> classes) {
        StringBuilder allLines = new StringBuilder();
        for(String className:classes)
            allLines.append(generateIncludeClassHeader(generateHeaderName(className)));
        allLines.append(newLine);
        allLines.append(generateMethodBody(completeClass.getAllMethodsBasedOnParents(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(generateConstructorBody(completeClass.getConstructors(), completeClass.getName()));
        allLines.append(newLine);
        if(completeClass.isHavingDestructor())
        {
            allLines.append(completeClass.generateDeleteBody());
            allLines.append(newLine);
        }

        return allLines.toString();
    }

    private String generateConstructorBody(Vector<CompleteConstructor> constructors, String className) {
        StringBuilder allLines = new StringBuilder();
        for(CompleteConstructor constructor:constructors)
            allLines.append(generateNewBody(constructor, className));
        return allLines.toString();
    }

    private String generateNewBody(CompleteConstructor constructor, String className) {
        StringBuilder allLines = new StringBuilder();
        allLines.append(constructor.generateConstructorReturnValue()).append(whiteSpace)
                .append(constructor.generateNewName())
                .append(constructor.getShowName());
        allLines.append(newLine + openCurlyBracket + newLine);
        allLines.append(tab).append(CompleteAttribute.generateAttributeThisText(className))
                .append(whiteSpace + equalSign + whiteSpace)
                .append(openParenthesis).append(constructor.generateConstructorReturnValue()).append(closeParenthesis)
                .append(whiteSpace + mallocKeyword + openParenthesis +
                        sizeofKeyword + openParenthesis + unionKeyword + whiteSpace)
                .append(className).append(closeParenthesis + closeParenthesis + semiColon + newLine);
        allLines.append(tab).append(constructor.generateConstructorName()).append(openParenthesis + thisKeyword);
        for(CompleteAttribute attribute:constructor.getParams())
            allLines.append(comma + whiteSpace).append(attribute.getName());
        allLines.append(closeParenthesis + semiColon + newLine);
        allLines.append(tab + returnKeyword + whiteSpace + thisKeyword + semiColon + newLine);
        allLines.append(closeCurlyBracket + newLine + newLine);

        return allLines.toString();
    }

    private String generateMethodBody(Vector<Pair<String, CompleteMethod>> methods, String className) {
        StringBuilder allLines = new StringBuilder();
        for(Pair<String, CompleteMethod> method:methods)
        {
            if(!method.getValue0().equals(className))
            {
                allLines.append(method.getValue1().generateMethodDefinition());
                allLines.append(newLine);
                allLines.append(method.getValue1().generateMethodUseInDefinition(method.getValue0()));
            }
        }
        return allLines.toString();
    }

    public boolean isSuccessFull() {
        return successFull;
    }

    public static final String EmptyBlock = newLine + openCurlyBracket + newLine +
            tab + lineComment + whiteSpace + "TODO:code here" + newLine +
            closeCurlyBracket + newLine + newLine;

    /// TODO move to CompleteClass
    public static String generateDestructor(String className)
    {
        String baseClassName = className + destruct + className + openParenthesis + closeParenthesis;
        return baseClassName + EmptyBlock;
    }

    /// TODO move to CompleteClass
    public static String generateHeaderName(String className)
    {
        return className + "Class.h";
    }

    /// TODO move to CompleteClass
    public static String generateClassHeaderFile(CompleteClass completeClass)
    {
        StringBuilder allLines = new StringBuilder();
        String fileName = generateHeaderName(completeClass.getName());
        allLines.append(generateIncludeGuard(fileName));
        allLines.append(newLine);
        allLines.append(generateIncludeClassHeader("AllClasses.h"));//TODO replace AllClasses.h with a PSVSf
        allLines.append(newLine);
        allLines.append(generateIncludesForAClass(completeClass.getParents(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(newLine);

        allLines.append(generateUnionDefinitions(completeClass, completeClass.getName()));
        allLines.append(newLine);
        allLines.append(
                generateMethodDefinitions(completeClass.getAllMethodsBasedOnParents(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(generateConstructorDefinitions(completeClass.getConstructors(), completeClass.getName()));
        allLines.append(newLine);
        if(completeClass.isHavingDestructor())
        {
            allLines.append(completeClass.generateDestructorDefinition());
            allLines.append(completeClass.generateDeleteDefinition());
            allLines.append(newLine);
        }

        allLines.append(generateEndGuard());
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateIncludesForAClass(Vector<String> classes, String theClassName)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classes)
            if(!className.equals(theClassName))
                allLines.append(generateIncludeClassHeader(generateHeaderName(className)));
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateUnionDefinitions(CompleteClass completeClass, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(unionKeyword + whiteSpace).append(completeClass.getName()).append(newLine)
                .append(openCurlyBracket).append(newLine);
        allLines.append(generateUnionUsage(completeClass.getParents(), className));

        allLines.append(tab + structKeyword + newLine);
        allLines.append(tab + openCurlyBracket + newLine);
        allLines.append(tab + tab).
                append(CompleteAttribute.generateAttributeThisText(className)).append(semiColon).append(newLine);
        for(CompleteAttribute attribute:completeClass.getAllAttributes())
            allLines.append(tab + tab).append(attribute.getShowName()).append(semiColon).append(newLine);
        allLines.append(tab + closeCurlyBracket + semiColon);
        allLines.append(newLine);

        allLines.append(closeCurlyBracket).append(semiColon);
        allLines.append(newLine);
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateMethodDefinitions(Vector<Pair<String, CompleteMethod>> methods, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(Pair<String, CompleteMethod> method:methods)
            allLines.append(method.getValue1().generateMethodDefinition()).append(semiColon).append(newLine);
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateConstructorDefinitions(Vector<CompleteConstructor> constructors, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(CompleteConstructor constructor:constructors)
        {
            allLines.append(constructor.generateConstructorDefinition()).append(semiColon).append(newLine);
            allLines.append(constructor.generateNewDefinition());
        }
        return allLines.toString();
    }

    /// TODO move to CompleteDiagram
    public static String generateAllClassesHeaderFile(Vector<String> classes, String fileName)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateIncludeGuard(fileName));
        allLines.append(newLine);
        allLines.append(sharp + includeKeyword + whiteSpace + lessThanSign + "stdio.h" + greaterThanSign + newLine);
        allLines.append(sharp + includeKeyword + whiteSpace + lessThanSign + "stdlib.h" + greaterThanSign + newLine);
        allLines.append(newLine);
        allLines.append(generateInlineDefinitionOfClass(classes));
        allLines.append(newLine);
        allLines.append(newLine);
        allLines.append(MethodOverloader.generateOverloadMacros());
        allLines.append(newLine);
        allLines.append(newLine);
        allLines.append(generateEndGuard());
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateOneInlineDefinitionOfClass(String className)
    {
        return unionKeyword + whiteSpace + className + semiColon;
    }

    /// TODO move to CompleteClass
    public static String generateInlineDefinitionOfClass(Vector<String> classNames)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classNames)
            allLines.append(generateOneInlineDefinitionOfClass(className)).append(newLine);

        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateOneUnionUsage(String className)
    {
        return tab + unionKeyword + whiteSpace + className + whiteSpace + unionKeyword + className + semiColon + newLine;
    }

    /// TODO move to CompleteClass
    public static String generateUnionUsage(Vector<String> classNames, String theClassName)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classNames)
            if(!className.equals(theClassName))
                allLines.append(generateOneUnionUsage(className));

        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateIncludeGuard(String headerFile)
    {
        String key = underscore + headerFile.toUpperCase().replace(dot, underscore) + underscore;
        return sharp + ifndef + whiteSpace + key + newLine +
                sharp + defineKeyword + whiteSpace + key + newLine;
    }

    /// TODO move to CompleteClass
    public static String generateEndGuard()
    {
        return sharp + endif + newLine;
    }

    /// TODO move to CompleteClass
    public static String generateIncludeClassHeader(String headerFile)
    {
        return sharp + includeKeyword + whiteSpace + doubleQuotation + headerFile + doubleQuotation + newLine;
    }

    /// TODO move to CompleteClass
    public static String generateClassCPP(CompleteClass completeClass)
    {
        StringBuilder base = new StringBuilder();
        for(CompleteConstructor constructor:completeClass.getConstructors())
            base.append(constructor.generateConstructor());
        for(CompleteMethod method:completeClass.getMethods())
            base.append(method.generateMethod());
        if(completeClass.isHavingDestructor())
            base.append(generateDestructor(completeClass.getName()));
        return base.toString();
    }

}
