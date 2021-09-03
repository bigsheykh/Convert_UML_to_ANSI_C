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

    public boolean isSuccessFull() {
        return successFull;
    }

    private static final String EmptyBlock = newLine + openCurlyBracket + newLine +
            tab + lineComment + whiteSpace + "TODO:code here" + newLine +
            closeCurlyBracket + newLine + newLine;

    /// TODO move to CompleteMethod
    public static String generateMethod(String className, CompleteMethod method)
    {
        String baseClassName = method.getReturnValueType().getShowName() + whiteSpace + className + doubleColon;
        return baseClassName +
                method.getShowName().substring(method.getReturnValueType().getShowName().length() + 1)
                + EmptyBlock;      // TODO use @StringBuilder
    }

    /// TODO move to CompleteConstructor
    public static String generateConstructor(String className, CompleteConstructor constructor)
    {
        String baseClassName = className + doubleColon + className;
        return baseClassName + constructor.getShowName() + EmptyBlock;      // TODO use @StringBuilder
    }

    /// TODO move to CompleteClass
    public static String generateDestructor(String className)
    {
        String baseClassName = className + destruct + className;
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
            allLines.append(generateDestructorDefinition(completeClass.getName()));
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
        allLines.append(unionKeyword + whiteSpace).append(completeClass.getName()).append(newLine).append(openCurlyBracket);
        allLines.append(generateUnionUsage(completeClass.getParents(), className));

        allLines.append(tab + structKeyword + newLine);
        allLines.append(tab + openCurlyBracket + newLine);
        for(CompleteAttribute attribute:completeClass.getAllAttributes())
            allLines.append(tab + tab).append(attribute.getShowName()).append(semiColon).append(newLine);
        allLines.append(tab + closeCurlyBracket);
        allLines.append(newLine);

        allLines.append(closeCurlyBracket);
        allLines.append(newLine);
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateMethodDefinitions(Vector<Pair<String, CompleteMethod>> methods, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(Pair<String, CompleteMethod> method:methods)
        {
            allLines.append(generateMethodDefinition(method.getValue1(), className)) ;
            if(method.getValue0().equals(className))
                allLines.append(semiColon).append(newLine);
            else
            {
                allLines.append(newLine);
                allLines.append(method.getValue1().generateMethodUseInDefinition(method.getValue0()));
            }
        }
        return allLines.toString();
    }

    /// TODO move to CompleteMethod
    public static String generateMethodDefinition(CompleteMethod method, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(method.getShowName(CompleteAttribute.generateAttributeThisText(className)));
        return allLines.toString();
    }

    /// TODO move to CompleteClass
    public static String generateDestructorDefinition(String className)
    {
        return generateDestructorName(className) +
                DescriptiveMember.generateParamsTogether(new Vector<>()
                        , CompleteAttribute.generateAttributeThisText(className)) + semiColon + newLine ;
    }

    /// TODO move to CompleteClass
    public static String generateConstructorDefinitions(Vector<CompleteConstructor> constructors, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(CompleteConstructor constructor:constructors)
            allLines.append(generateConstructorDefinition(constructor, className)).append(semiColon).append(newLine);
        return allLines.toString();
    }

    // TODO move to CompleteConstructor
    public static String generateConstructorDefinition(CompleteConstructor constructor, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateConstructorName(className))
                .append(constructor.getShowName(CompleteAttribute.generateAttributeThisText(className).replace(openParenthesis,
                        openParenthesis + unionKeyword + whiteSpace +  className + whiteSpace + comma)));
        return allLines.toString();
    }

    /// TODO move to CompleteConstructor
    public static String generateConstructorName(String className)
    {
        return constructorKeyword + className;
    }

    /// TODO move to CompleteClass
    public static String generateDestructorName(String className)
    {
        return destructorKeyword + className;
    }

    /// TODO move to CompleteDiagram
    public static String generateAllClassesHeaderFile(Vector<String> classes, String fileName)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateIncludeGuard(fileName));
        allLines.append(newLine);
        allLines.append(generateInlineDefinitionOfClass(classes));
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
            base.append(generateConstructor(completeClass.getName(), constructor));
        for(CompleteMethod method:completeClass.getMethods())
            base.append(generateMethod(completeClass.getName(), method));
        if(completeClass.isHavingDestructor())
            base.append(generateDestructor(completeClass.getName()));
        return base.toString();
    }

}
