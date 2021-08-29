package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.DescriptiveMember;
import com.project.classBaseUML.ValueType;

import java.io.*;
import java.util.Vector;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

///TODO delete this and move all functions into Complete* classes
public class Phase1FileGenerator {

    boolean successFull;

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
        allLines.append(generateIncludesForAClass(completeClass.getParents(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(newLine);

        allLines.append(generateUnionDefinitions(completeClass, completeClass.getName()));
        allLines.append(newLine);
        allLines.append(generateMethodDefinitions(completeClass.getAllMethods(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(generateConstructorDefinitions(completeClass.getConstructors(), completeClass.getName()));
        allLines.append(newLine);
        allLines.append(generateDestructorDefinition(completeClass.getName()));
        allLines.append(newLine);

        allLines.append(generateEndGuard());
        return allLines.toString();
    }

    private String generateIncludesForAClass(Vector<String> classes, String theClassName)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classes)
            if(!className.equals(theClassName))
                allLines.append(generateIncludeClassHeader(generateHeaderName(className)));
        return allLines.toString();
    }

    private String generateUnionDefinitions(CompleteClass completeClass, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(union + whiteSpace).append(completeClass.getName()).append(newLine).append(openCurlyBracket);
        allLines.append(generateUnionUsage(completeClass.getParents(), className));

        allLines.append(tab + struct + newLine);
        allLines.append(tab + openCurlyBracket + newLine);
        for(CompleteAttribute attribute:completeClass.getAllAttributes())
            allLines.append(tab + tab).append(attribute.getShowName()).append(semiColon).append(newLine);
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
            allLines.append(generateMethodDefinition(method, className)).append(semiColon).append(newLine);
        return allLines.toString();
    }

    private String generateMethodDefinition(CompleteMethod method, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(method.getShowName(generateAttributeThis(className)).replace(openParenthesis,
                openParenthesis + union + whiteSpace +  className + whiteSpace + comma));
        return allLines.toString();
    }

    private String generateDestructorDefinition(String className)
    {
        return generateDestructorName(className) +
                DescriptiveMember.generateParamsTogether(new Vector<>(), generateAttributeThis(className)) +
                newLine + semiColon;
    }

    private String generateConstructorDefinitions(Vector<CompleteConstructor> constructors, String className)
    {
        StringBuilder allLines = new StringBuilder();
        for(CompleteConstructor constructor:constructors)
            allLines.append(generateConstructorDefinition(constructor, className)).append(semiColon).append(newLine);
        return allLines.toString();
    }

    private String generateAttributeThis(String className)
    {
        ValueType valueType = new ValueType(union + whiteSpace + className, 1);
        ClassAttribute<ValueType> attribute = new ClassAttribute<>(valueType, thisKeyword);
        CompleteAttribute completeAttribute = new CompleteAttribute(attribute);
        return completeAttribute.getShowName();
    }

    private String generateConstructorDefinition(CompleteConstructor constructor, String className)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateConstructorName(className))
                .append(constructor.getShowName(generateAttributeThis(className).replace(openParenthesis,
                        openParenthesis + union + whiteSpace +  className + whiteSpace + comma)));
        return allLines.toString();
    }

    private String generateConstructorName(String className)
    {
        return constructor + className;
    }

    private String generateDestructorName(String className)
    {
        return destructor + className;
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

    private String generateUnionUsage(Vector<String> classNames, String theClassName)
    {
        StringBuilder allLines = new StringBuilder();
        for(String className:classNames)
            if(!className.equals(theClassName))
                allLines.append(generateOneUnionUsage(className));

        return allLines.toString();
    }

    private String generateIncludeGuard(String headerFile)
    {
        String key = underscore + headerFile.toUpperCase().replace(Dot, underscore) + underscore;
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
