package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ClassConstructor;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;
import static com.project.lexicalAnalyzer.CLanguageTokens.star;
import static com.project.phase1CodeGeneration.Phase1CodeGenerator.EmptyBlock;

public class CompleteConstructor extends ClassConstructor<CompleteValueType, CompleteAttribute> {
    private final String className;
    private final String hashAdded;
    public CompleteConstructor(ClassConstructor constructor, String className) {
        super();
        this.className = className;
        for(Object param:constructor.getParams())
            getParams().add(new CompleteAttribute((ClassAttribute) param));
        hashAdded = MethodOverloader.randomGenerator();
        MethodOverloader.addToTable(generateConstructorRealName(), generateConstructorName(), className, getParams());
        MethodOverloader.addToTable(generateNewRealName(), generateNewName(), null, getParams());
    }

    public String generateConstructorName()
    {
        return constructorKeyword + className + hashAdded;
    }

    public String generateConstructorRealName()
    {
        return constructorKeyword + className;
    }

    public String generateConstructor()
    {
        String baseClassName = className + doubleColon + className + hashAdded;
        return baseClassName + getShowName() + EmptyBlock;      // TODO use @StringBuilder
    }

    public String generateConstructorReturnValue()
    {
        return unionKeyword + whiteSpace + className + star;
    }

    public String generateNewDefinition()
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(generateConstructorReturnValue()).append(whiteSpace).append(generateNewName())
                .append(getShowName()).append(semiColon).append(newLine);
        return allLines.toString();
    }


    public String generateNewRealName()
    {
        return newKeyword + className;
    }

    public String generateNewName()
    {
        return generateNewRealName() + hashAdded;
    }

    public String generateConstructorDefinition()
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(voidKeyword + whiteSpace).append(generateConstructorName())
                .append(getShowName(CompleteAttribute.generateAttributeThisText(className)
                        .replace(openParenthesis, openParenthesis + unionKeyword + whiteSpace +
                                className + whiteSpace + comma)));
        return allLines.toString();
    }

}
