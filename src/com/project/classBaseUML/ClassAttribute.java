package com.project.classBaseUML;

import com.project.lexicalAnalyzer.LexicalAnalyzer;
import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Vector;

import static com.project.Main.document;

public class ClassAttribute<TType extends ValueType> implements DescriptiveMember, Comparable<ClassAttribute> {
    TType valueType;
    String name;

    public ClassAttribute(TType valueType, String name) {
        this.valueType = valueType;
        this.name = name;
    }

    public ClassAttribute() {
        valueType = (TType) new ValueType();
    }

    public TType getValueType() {
        return valueType;
    }

    public void setValueType(TType valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getElementDocument() {
        Element root = document.createElement("Attribute");
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(getName()));
        root.appendChild(name);
        root.appendChild(getValueType().getElementDocument());
        return root;
    }

    @Override
    public void setDataByNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            switch (currentNode.getNodeName()) {
                case "name":
                    if (currentNode.getFirstChild() != null)
                        setName(currentNode.getFirstChild().getNodeValue());
                    break;
                case "Type":
                    TType valueType = (TType) new ValueType();
                    valueType.setDataByNode(currentNode);
                    setValueType(valueType);
                    break;
                default:
                    System.out.println("Attribute:" + currentNode.getNodeName());
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
        if (!LexicalAnalyzer.isNameOkayInC(getName()))
            return DescriptiveMember.newStatus(BasicDiagramStatus.AttributeNameError,
                    "attribute:" + getName() + " name is invalid.");
        return DescriptiveMember.addDescriptionStatus(getValueType().statusOfMember(),
                "attribute:" + getName() + " ->");
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> vector = new Vector<>();
        if (!LexicalAnalyzer.isNameOkayInC(getName()))
            vector.add(DescriptiveMember.newStatus(BasicDiagramStatus.AttributeNameError,
                    "attribute:" + getName() + " name is invalid."));
        vector.addAll(DescriptiveMember.addDescriptionStatus(getValueType().getAllProblems(),
                "attribute:" + getName() + " ->"));
        return vector;
    }


    @Override
    public int compareTo(ClassAttribute classAttribute) {
        if (classAttribute.getValueType().compareTo(getValueType()) != 0)
            return getValueType().compareTo(classAttribute.getValueType());
        return getName().compareTo(classAttribute.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassAttribute)) return false;
        ClassAttribute<?> attribute = (ClassAttribute<?>) o;
        return Objects.equals(name, attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String getShowName(String... className) {
        return getValueType().getShowName() + " " + getName();
    }

}
