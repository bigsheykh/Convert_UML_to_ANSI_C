package com.project.Phase1CodeGeneration;

import com.project.classBaseUML.ValueType;

public class CompleteValueType extends ValueType {
    public CompleteValueType(ValueType valueType) {
        setTypeName(valueType.getTypeName());
        setNumberOfPointer(valueType.getNumberOfPointer());
    }
}
