package com.project.Phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;

public class CompleteAttribute extends ClassAttribute<CompleteValueType> {
    public CompleteAttribute(ClassAttribute attribute) {
        setValueType(new CompleteValueType(attribute.getValueType()));
        setName(attribute.getName());
    }
}
