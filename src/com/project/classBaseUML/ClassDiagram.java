package com.project.classBaseUML;

import com.project.graphBaseDependency.DependencyEdge;
import com.project.graphBaseDependency.GraphOperation;
import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.Vector;

import static com.project.Main.document;

public class ClassDiagram<TType extends ValueType, TAttribute extends ClassAttribute<TType>,
        TConstructor extends ClassConstructor<TType, TAttribute>,
        TMethod extends ClassMethod<TType, TAttribute>,
        TClass extends ClassStructure<TType, TAttribute, TConstructor, TMethod>> implements DescriptiveMember {

    Vector<TClass> classes;

    public ClassDiagram() {
        classes = new Vector<>();
    }

    public Vector<TClass> getClasses() {
        return classes;
    }

    public void setClasses(Vector<TClass> classes) {
        this.classes = classes;
    }

    @Override
    public Element getElementDocument() {
        Element root = document.createElement("ClassDiagram");
        for (TClass classStructure : classes)
            root.appendChild(classStructure.getElementDocument());
        return root;
    }

    @Override
    public BasicDiagramStatus getStatusType() {
        return statusOfMember().getValue0();
    }

    @Override
    public Pair<BasicDiagramStatus, LinkedList<String>> statusOfMember() {
        Pair<BasicDiagramStatus, LinkedList<String>> status = DescriptiveMember.statusOfVector(
                getClasses(), true, "class", BasicDiagramStatus.SameClassName);
        if (status.getValue0() != BasicDiagramStatus.Okay)
            return status;
        Vector<String> allClasses = allClassNames();
        for (TClass tClass : getClasses())
            if (tClass.havingSuperClass() && !allClasses.contains(tClass.getSuperClass()))
                return DescriptiveMember.newStatus(BasicDiagramStatus.NonExistSuperClass,
                        "super Class name:" + tClass.getSuperClass() + " class name:" + tClass.getName());
            else
            {
                for(TAttribute tAttribute:tClass.getAttributes())
                    if(allClasses.contains(tAttribute.getName()))
                        return DescriptiveMember.newStatus(BasicDiagramStatus.SameAttributeAndClassName,
                                "class name is:" + tAttribute.getName());
                for(TMethod tMethod:tClass.getMethods())
                    if(allClasses.contains(tMethod.getName()))
                        return DescriptiveMember.newStatus(BasicDiagramStatus.SameMethodAndClassName,
                                "name is:" + tMethod.getName());
            }
        return DescriptiveMember.okStatus();
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> pairVector = DescriptiveMember.getAllProblemsOfVector(
                getClasses(), true, "class", BasicDiagramStatus.SameClassName);
        Vector<String> allClasses = allClassNames();
        for (TClass tClass : getClasses())
        {
            if (tClass.havingSuperClass() && !allClasses.contains(tClass.getSuperClass()))
                pairVector.add(DescriptiveMember.newStatus(BasicDiagramStatus.NonExistSuperClass,
                        "super Class name:" + tClass.getSuperClass() + " class name:" + tClass.getName()));
            for(TAttribute tAttribute:tClass.getAttributes())
                if(allClasses.contains(tAttribute.getName()))
                    pairVector.add(DescriptiveMember.newStatus(BasicDiagramStatus.SameAttributeAndClassName,
                            "name is:" + tAttribute.getName()));
            for(TMethod tMethod:tClass.getMethods())
                if(allClasses.contains(tMethod.getName()))
                    pairVector.add(DescriptiveMember.newStatus(BasicDiagramStatus.SameMethodAndClassName,
                            "name is:" + tMethod.getName()));
        }
        return pairVector;
    }

    @Override
    public String getShowName(String... className) {
        return "";
    }

    public Vector<String> allClassNames() {
        Vector<String> classNames = new Vector<>();
        for (TClass tClass : getClasses())
            classNames.add(tClass.getName());
        return classNames;
    }

    public Vector<DependencyEdge> allDependencyEdges() {
        Vector<DependencyEdge> edges = new Vector<>();
        for (TClass tClass : getClasses())
            edges.addAll(tClass.getAllDependenciesEdges());
        return edges;
    }

    public GraphOperation getResultOfGraphOperation()
    {
        return new GraphOperation(allClassNames(), allDependencyEdges());
    }

    public void setDataByNode(Node node) {
        setClasses(new Vector<>());
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeName().equals("Class")) {
                TClass newClass = (TClass) new ClassStructure<TType, TAttribute, TConstructor, TMethod>();
                newClass.setDataByNode(currentNode);
                getClasses().add(newClass);
            }
        }
    }
}
