package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;

public class CompleteAttribute extends ClassAttribute<CompleteValueType> {
    public CompleteAttribute(ClassAttribute attribute) {
        setValueType(new CompleteValueType(attribute.getValueType()));
        setName(attribute.getName());
    }
}
