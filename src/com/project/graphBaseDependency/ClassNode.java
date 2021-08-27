package com.project.graphBaseDependency;

import java.util.Vector;

public class ClassNode {
    private final Vector <DependencyEdge> edges;
    private final String name;
    private boolean visit;
    private int dependencyCycleType;

    public ClassNode(Vector<DependencyEdge> allEdges, String name) {
        this.name = name;
        this.edges = new Vector<>();
        for (DependencyEdge edge:allEdges)
            if(edge.getDependentNode().equals(name))
                this.edges.add(edge);
        dependencyCycleType = 3;

    }

    public void reset()
    {
        visit = false;
    }

    public Vector<DependencyEdge> getEdges(int upperType) {
        if(upperType >= 2)
            return edges;
        Vector<DependencyEdge> upperEdges = new Vector<>();
        for(DependencyEdge edge:edges)
            if(edge.getDependencyType() <= upperType)
                upperEdges.add(edge);
        return upperEdges;
    }

    public String getName() {
        return name;
    }

    public boolean isVisit() {
        return visit;
    }

    public void setVisit(boolean visit) {
        this.visit = visit;
    }

    public int getDependencyCycleType() {
        return dependencyCycleType;
    }

    public void setDependencyCycleType(int dependencyCycleType) {
        this.dependencyCycleType = dependencyCycleType;
    }
}
