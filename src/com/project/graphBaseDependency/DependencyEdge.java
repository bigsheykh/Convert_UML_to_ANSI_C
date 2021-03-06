package com.project.graphBaseDependency;

import static com.project.lexicalAnalyzer.LexicalAnalyzer.*;

public class DependencyEdge {
    private final DependencyStatus edgeType;
    private final String dependentNode;
    private final String fatherNode;
    public DependencyEdge(DependencyStatus edgeType, String dependentNode, String fatherNode) {
        this.edgeType = edgeType;
        this.dependentNode = unStruct(deleteClassSpecifier(dependentNode));
        this.fatherNode = unStruct(deleteClassSpecifier(fatherNode));
    }

    @Override
    public String toString() {
        return "DependencyEdge{" +
                "edgeType=" + edgeType +
                ", dependentNode='" + dependentNode + '\'' +
                ", fatherNode='" + fatherNode + '\'' +
                '}';
    }

    public DependencyStatus getEdgeType() {
        return edgeType;
    }

    public String getDependentNode() {
        return dependentNode;
    }

    public String getFatherNode() {
        return fatherNode;
    }

    public int getDependencyType()
    {
        return edgeType.getDependencyType();
    }
}
