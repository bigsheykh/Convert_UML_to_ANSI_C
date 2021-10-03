package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ClassMethod;
import com.project.classBaseUML.DescriptiveMember;
import com.project.classBaseUML.ValueType;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;
import static com.project.phase1CodeGeneration.Phase1CodeGenerator.*;

public class CompleteMethod extends ClassMethod<CompleteValueType, CompleteAttribute> {

    private final String className;
    private final String hashAdded;
    private final String realName;
    public CompleteMethod(ClassMethod method, String className) {
        super();
        this.className = className;
        for(Object param:method.getParams())
            getParams().add(new CompleteAttribute((ClassAttribute) param));
        hashAdded = MethodOverloader.randomGenerator();
        realName = method.getName();
        setName(generateMethodName());
        setReturnValueType(new CompleteValueType(method.getReturnValueType()));
        MethodOverloader.addToTable(realName, getName(), className, getParams());
    }


    private String generateMethodName()
    {
        return realName + hashAdded;
    }

    public String generateMethodUseInDefinition(String parentClassName)
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(openCurlyBracket);
        allLines.append(newLine).append(tab);
        if(!getReturnValueType().equals(new ValueType(voidKeyword, 0)))
            allLines.append(returnKeyword).append(whiteSpace);
        allLines.append(realName);
        allLines.append(openParenthesis);
        allLines.append(and).append(openParenthesis)
                .append(thisKeyword).append(arrow).append(unionKeyword).append(parentClassName)
                .append(closeParenthesis);
        for(CompleteAttribute completeAttribute:getParams())
            allLines.append(comma).append(whiteSpace).append(completeAttribute.getName());
        allLines.append(closeParenthesis);
        allLines.append(semiColon).append(newLine).append(closeCurlyBracket).append(newLine);
        return allLines.toString();
    }

    public String generateMethodDefinition()
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(getShowName(CompleteAttribute.generateAttributeThisText(className)));
        return allLines.toString();
    }

    public String generateMethod()
    {
        String baseClassName = getReturnValueType().getShowName() + whiteSpace + className + doubleColon;
        return baseClassName +
                getShowName().substring(getReturnValueType().getShowName().length() + 1)
                + EmptyBlock;      // TODO use @StringBuilder
    }


}
