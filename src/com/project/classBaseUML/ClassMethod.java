package com.project.classBaseUML;

import com.project.lexicalAnalyzer.LexicalAnalyzer;
import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

import static com.project.Main.document;
import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public class ClassMethod<TType extends ValueType, T extends ClassAttribute<TType>> implements DescriptiveMember,
        Comparable<ClassMethod> {
    String name;
    TType returnValueType;
    Vector<T> params;

    public ClassMethod(String name, TType returnValueType) {
        this.name = name;
        this.returnValueType = returnValueType;
        params = new Vector<>();
    }

    public ClassMethod() {
        returnValueType = (TType) new ValueType();
        params = new Vector<>();
    }

    public ClassMethod(String name, TType returnValueType, Vector<T> params) {
        this.name = name;
        this.returnValueType = returnValueType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TType getReturnValueType() {
        return returnValueType;
    }

    public void setReturnValueType(TType returnValueType) {
        this.returnValueType = returnValueType;
    }

    public Vector<T> getParams() {
        return params;
    }

    public void setParams(Vector<T> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassMethod)) return false;
        ClassMethod<?, ?> that = (ClassMethod<?, ?>) o;
        if (!name.equals(that.name) || that.getParams().size() != getParams().size())
            return false;

        for (int i = 0; i < that.getParams().size(); i++)
            if (!that.getParams().get(i).getValueType().equals(getParams().get(i).getValueType()))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnValueType, params);
    }

    public Element getElementDocument() {
        Element root = document.createElement("Method");
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(getName()));
        root.appendChild(name);
        root.appendChild(getReturnValueType().getElementDocument());
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
            switch (currentNode.getNodeName()) {
                case "name":
                    if (currentNode.getFirstChild() != null)
                        setName(currentNode.getFirstChild().getNodeValue());
                    break;
                case "params":
                    allNodesList = currentNode.getChildNodes();
                    for (int j = 0; j < allNodesList.getLength(); j++) {
                        Node TNode = allNodesList.item(j);
                        if (TNode.getNodeName().equals("Attribute")) {
                            T attribute = (T) new ClassAttribute<TType>();
                            attribute.setDataByNode(TNode);
                            getParams().add(attribute);
                        }
                    }
                    break;
                case "Type":
                    TType valueType = (TType) new ValueType();
                    valueType.setDataByNode(currentNode);
                    setReturnValueType(valueType);
                    break;
                default:
                    System.out.println("Method:" + currentNode.getNodeName());
                    break;
            }
        }
    }

    @Override
    public BasicDiagramStatus getStatusType() {
        return statusOfMember().getValue0();
    }

    @Override
    public Pair<BasicDiagramStatus, LinkedList<String>> statusOfMember() {
        Pair<BasicDiagramStatus, LinkedList<String>> status = DescriptiveMember.statusOfVector(
                getParams(), true, "parameter", BasicDiagramStatus.SameParameterName);
        if (status.getValue0() != BasicDiagramStatus.Okay)
            return status;
        Pair<BasicDiagramStatus, LinkedList<String>> pair = getReturnValueType().statusOfMember();
        if (pair.getValue0() != BasicDiagramStatus.Okay)
            return DescriptiveMember.addDescriptionStatus(pair, "method's: value type->");
        if (!LexicalAnalyzer.isNameOkayInC(getName()))
            return DescriptiveMember.newStatus(BasicDiagramStatus.MethodNameError,
                    "method:" + getName() + " name is invalid.");
        return DescriptiveMember.okStatus();
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> vector =
                new Vector<>(DescriptiveMember.getAllProblemsOfVector(
                        getParams(), true, "parameter", BasicDiagramStatus.SameParameterName));
        if (!LexicalAnalyzer.isNameOkayInC(getName()))
            vector.add(DescriptiveMember.newStatus(BasicDiagramStatus.TypeNameError,
                    "method:" + getName() + " name is invalid."));
        vector.addAll(DescriptiveMember.addDescriptionStatus(getReturnValueType().getAllProblems(),
                "method's: value type->"));
        return vector;
    }


    @Override
    public int compareTo(ClassMethod classMethod) {
        if (!classMethod.getName().equals(getName()))
            return getName().compareTo(classMethod.getName());
        if (classMethod.getReturnValueType().compareTo(getReturnValueType()) != 0)
            return getReturnValueType().compareTo(classMethod.getReturnValueType());
        if (classMethod.getParams().size() != getParams().size())
            return getParams().size() - classMethod.getParams().size();

        for (int i = 0; i < classMethod.getParams().size(); i++)
            if (((ClassAttribute<?>) (classMethod.getParams().get(i))).compareTo(getParams().get(i)) != 0)
                return ((ClassAttribute<?>) (classMethod.getParams().get(i))).compareTo(getParams().get(i));
        return 0;
    }

    @Override
    public String getShowName(String... className) {
        StringBuilder returnValue = new StringBuilder();
        returnValue.append(
                getReturnValueType().getShowName()).append(whiteSpace).append(getName());
        returnValue.append(DescriptiveMember.generateParamsTogether(getParams(), className));
        return returnValue.toString();
    }

}
