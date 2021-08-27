package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ClassConstructor;

public class CompleteConstructor extends ClassConstructor<CompleteValueType, CompleteAttribute> {
    public CompleteConstructor(ClassConstructor constructor) {
        super();
        for(Object param:constructor.getParams())
            getParams().add(new CompleteAttribute((ClassAttribute) param));
    }
}
