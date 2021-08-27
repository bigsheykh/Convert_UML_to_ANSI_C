package com.project.classBaseUML;

public enum BasicDiagramStatus {
    Okay(0),
    SameConstructor(-1),
    SameMethodSignature(-2),
    SameAttributeName(-3),
    SameAttributeAndMethodName(-4),
    SameAttributeAndClassName(-5),
    SameMethodAndClassName(-6),
    SameClassName(-7),
    SameParameterName(-8),
    NegativeValueTypePointer(-9),
    TypeNameError(-10),
    AttributeNameError(-11),
    MethodNameError(-12),
    ClassNameError(-13),
    SuperClassNameError(-14),
    NonExistSuperClass(-15)
    ;

    private final int basicDiagramStatusCode;

    BasicDiagramStatus(int i) {
        this.basicDiagramStatusCode = i;
    }

    public int getBasicDiagramStatusCode() {
        return this.basicDiagramStatusCode;
    }
}
