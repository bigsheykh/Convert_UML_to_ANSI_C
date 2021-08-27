package com.project.graphBaseDependency;

import org.javatuples.Pair;

public enum DependencyStatus {
    SuperClass(new Pair<>(0, 0)),
    ObjectAttribute(new Pair<>(0, 1)),
    PointerAttribute(new Pair<>(2, 2)),
    MethodObjectType(new Pair<>(2, 3)),
    MethodPointerType(new Pair<>(2, 4)),
    MethodObjectParameter(new Pair<>(2, 5)),
    MethodPointerParameter(new Pair<>(2, 6)),
    ConstructorAllObjectParameter(new Pair<>(0, 7)),
    ConstructorAllPointerParameter(new Pair<>(1, 8)),
    ConstructorAllHybridParameter(new Pair<>(1, 9)),
    ConstructorObjectParameter(new Pair<>(2, 10)),
    ConstructorPointerParameter(new Pair<>(2, 11)),
    ;

    private final Pair<Integer, Integer> dependencyStatusCode;

    DependencyStatus(Pair<Integer, Integer> i) {
        this.dependencyStatusCode = i;
    }

    public Pair<Integer, Integer> getDependencyStatusCode() {
        return dependencyStatusCode;
    }

    public int getDependencyType()
    {
        return getDependencyStatusCode().getValue0();
    }
}
