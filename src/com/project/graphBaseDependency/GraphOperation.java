package com.project.graphBaseDependency;

import java.util.HashMap;
import java.util.Vector;

public class GraphOperation {
    private final HashMap<String, ClassNode> nodes;
    private final Vector<String> classNames;
    private final Vector<Vector<DependencyEdge>>[] allCycles;
    private int dependencyNumber;

    public GraphOperation(Vector<String> classNames, Vector<DependencyEdge> allDependencyEdge) {
        this.classNames = classNames;
        nodes = new HashMap<>();
        allCycles = new Vector[4];
        for (int i = 0; i < 4; i++)
            allCycles[i] = new Vector<>();
        for(String className:classNames)
            nodes.put(className, new ClassNode(allDependencyEdge, className));
        findMostCycles();
    }

    public Vector<Vector<DependencyEdge>>[] getAllCycles() {
        return allCycles;
    }

    public int getDependencyNumber() {
        return dependencyNumber;
    }

    private void findMostCycles()
    {
        dependencyNumber = 3;
        for (String name:classNames)
        {
            for(int dependencyType = 0; dependencyType < nodes.get(name).getDependencyCycleType(); dependencyType++)
            {
                for(String node:classNames)
                    nodes.get(node).reset();
                Vector<DependencyEdge> dfsResult = dfs(name,name,dependencyType);
                if (dfsResult != null)
                {
                    allCycles[dependencyType].add(dfsResult);
                    dependencyNumber = Math.min(dependencyNumber, dependencyType);
                }
            }
        }
    }

    private Vector<DependencyEdge> dfs(String v, String start, int dependencyType)
    {
        ClassNode vNode = nodes.get(v);
        if (vNode == null)
            return null;
        if (v.equals(start) && vNode.isVisit())
            return new Vector<>();
        if (vNode.isVisit())
            return null;
        vNode.setVisit(true);
        for(DependencyEdge edge:vNode.getEdges(dependencyType))
        {
            Vector<DependencyEdge> dependencyEdges = dfs(edge.getFatherNode(), start, dependencyType);
            if(dependencyEdges != null)
            {
                vNode.setDependencyCycleType(dependencyType);
                dependencyEdges.add(edge);
                return dependencyEdges;
            }
        }
        return null;
    }
}
