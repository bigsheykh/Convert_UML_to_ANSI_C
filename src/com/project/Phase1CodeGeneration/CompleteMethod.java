package com.project.Phase1CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ClassMethod;

public class CompleteMethod extends ClassMethod<CompleteValueType, CompleteAttribute> {
    public CompleteMethod(ClassMethod method) {
        super();
        for(Object param:method.getParams())
            getParams().add(new CompleteAttribute((ClassAttribute) param));
        setName(method.getName());
        setReturnValueType(new CompleteValueType(method.getReturnValueType()));
    }
}
