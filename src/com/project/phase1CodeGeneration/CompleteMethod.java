package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ClassMethod;
import com.project.classBaseUML.DescriptiveMember;
import com.project.classBaseUML.ValueType;

import java.util.Objects;

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
        if(method instanceof CompleteMethod)
        {
            hashAdded = ((CompleteMethod) method).hashAdded;
            realName = ((CompleteMethod) method).realName;
        }
        else
        {
            hashAdded = MethodOverloader.randomGenerator();
            realName = method.getName();
        }
        setName(generateMethodName());
        setReturnValueType(new CompleteValueType(method.getReturnValueType()));
        }

    public String getRealName() {
        return realName;
    }

    public void addToTable()
    {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompleteMethod)) return false;
        CompleteMethod that = (CompleteMethod) o;
        if (!realName.equals(that.realName) || that.getParams().size() != getParams().size())
            return false;

        for (int i = 0; i < that.getParams().size(); i++)
            if (!that.getParams().get(i).getValueType().equals(getParams().get(i).getValueType()))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), realName);
    }
}
