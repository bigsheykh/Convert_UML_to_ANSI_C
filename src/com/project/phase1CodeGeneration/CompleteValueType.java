package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ValueType;

public class CompleteValueType extends ValueType {
    public CompleteValueType(ValueType valueType) {
        setTypeName(valueType.getTypeName());
        setNumberOfPointer(valueType.getNumberOfPointer());
    }
}
