package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ValueType;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public class CompleteAttribute extends ClassAttribute<CompleteValueType> {
    public CompleteAttribute(ClassAttribute attribute) {
        setValueType(new CompleteValueType(attribute.getValueType()));
        setName(attribute.getName());
    }

    public static CompleteAttribute generateAttributeThis(String className)
    {
        ValueType valueType = new ValueType(unionKeyword + whiteSpace + className, 1);
        ClassAttribute<ValueType> attribute = new ClassAttribute<>(valueType, thisKeyword);
        return new CompleteAttribute(attribute);
    }

    public static String generateAttributeThisText(String className)
    {
        return generateAttributeThis(className).getShowName();
    }
}
