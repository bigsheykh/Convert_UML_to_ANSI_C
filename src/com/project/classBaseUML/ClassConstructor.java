package com.project.classBaseUML;

import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

import static com.project.Main.document;

public class ClassConstructor<TType extends ValueType, T extends ClassAttribute<TType>> implements DescriptiveMember {
    Vector<T> params;

    public ClassConstructor() {
        params = new Vector<>();
    }

    public ClassConstructor(Vector<T> params) {
        this.params = params;
    }

    public Vector<T> getParams() {
        return params;
    }

    public void setParams(Vector<T> params) {
        this.params = params;
    }

    public Set<String> allParamTypesWithoutPointer()
    {
        Set<String> returnValue = new HashSet<>();
        for (T param : getParams())
            if(param.getValueType().getNumberOfPointer() == 0)
                returnValue.add(param.getValueType().getTypeName());
        return returnValue;
    }

    public Set<String> allParamTypesWithPointer()
    {
        Set<String> returnValue = new HashSet<>();
        for (T param : getParams())
            if(param.getValueType().getNumberOfPointer() > 0)
                returnValue.add(param.getValueType().getTypeName());
        return returnValue;
    }

    public Set<String> allParamTypes()
    {
        Set<String> returnValue =  allParamTypesWithoutPointer();
        returnValue.addAll(allParamTypesWithPointer());
        return returnValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassConstructor)) return false;
        ClassConstructor<?, ?> that = (ClassConstructor<?, ?>) o;
        if (that.getParams().size() != getParams().size())
            return false;
        for (int i = 0; i < that.getParams().size(); i++)
            if (!that.getParams().get(i).getValueType().equals(getParams().get(i).getValueType()))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }

    public Element getElementDocument() {
        Element root = document.createElement("Constructor");
        Element allParams = document.createElement("params");
        for (T param : getParams())
            allParams.appendChild(param.getElementDocument());
        root.appendChild(allParams);
        return root;
    }

    @Override
    public void setDataByNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            NodeList allNodesList;
            if ("params".equals(currentNode.getNodeName())) {
                allNodesList = currentNode.getChildNodes();
                for (int j = 0; j < allNodesList.getLength(); j++) {
                    Node TNode = allNodesList.item(j);
                    if (TNode.getNodeName().equals("Attribute")) {
                        T attribute = (T) new ClassAttribute<TType>();
                        attribute.setDataByNode(TNode);
                        getParams().add(attribute);
                    }
                }
            } else
                System.out.println("constructor:" + currentNode.getNodeName());
        }
    }

    @Override
    public BasicDiagramStatus getStatusType() {
        return statusOfMember().getValue0();
    }

    @Override
    public Pair<BasicDiagramStatus, LinkedList<String>> statusOfMember() {
        return DescriptiveMember.statusOfVector(
                getParams(), true, "parameter", BasicDiagramStatus.SameParameterName);
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        return DescriptiveMember.getAllProblemsOfVector(
                getParams(), true, "parameter", BasicDiagramStatus.SameParameterName);
    }

    @Override
    public String getShowName() {
        StringBuilder returnValue = new StringBuilder();
        returnValue.append("(");
        boolean moreThanOnce = false;
        for (ClassAttribute<?> attribute : getParams())
            if (moreThanOnce)
                returnValue.append(", ").append(attribute.getShowName());
            else {
                moreThanOnce = true;
                returnValue.append(attribute.getShowName());
            }
        return returnValue + ")";
    }

}
