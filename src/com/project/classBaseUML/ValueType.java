package com.project.classBaseUML;

import com.project.lexicalAnalyzer.LexicalAnalyzer;
import org.javatuples.Pair;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;

import static com.project.Main.document;

public class ValueType implements DescriptiveMember, Comparable<ValueType> {
    private String typeName;
    private int numberOfPointer;

    public ValueType() {
    }

    public ValueType(String typeName, int numberOfPointer) {
        this.typeName = typeName;
        this.numberOfPointer = numberOfPointer;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getNumberOfPointer() {
        return numberOfPointer;
    }

    public void setNumberOfPointer(int numberOfPointer) {
        this.numberOfPointer = numberOfPointer;
    }

    public Element getElementDocument() {
        Element root = document.createElement("Type");
        Element typeName = document.createElement("typename");
        typeName.appendChild(document.createTextNode(getTypeName()));
        root.appendChild(typeName);
        Element numberOfPointers = document.createElement("number");
        numberOfPointers.appendChild(document.createTextNode(String.valueOf(getNumberOfPointer())));
        root.appendChild(numberOfPointers);
        return root;
    }

    @Override
    public void setDataByNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            switch (currentNode.getNodeName()) {
                case "typename":
                    if (currentNode.getFirstChild() != null)
                        setTypeName(currentNode.getFirstChild().getNodeValue());
                    break;
                case "number":
                    if (currentNode.getFirstChild() != null &&
                            Pattern.matches("[0-9]+", currentNode.getFirstChild().getNodeValue()))
                        setNumberOfPointer(Integer.parseInt(currentNode.getFirstChild().getNodeValue()));
                    else
                        setNumberOfPointer(-1);
                    break;
                default:
                    System.out.println("ValueType:" + currentNode.getNodeValue());
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
        if (getNumberOfPointer() < 0)
            return DescriptiveMember.newStatus(BasicDiagramStatus.NegativeValueTypePointer,
                    "typeName:" + getTypeName() + " number is negative.");
        if (LexicalAnalyzer.isTypeOkayInC(getTypeName()))
            return DescriptiveMember.okStatus();
        return DescriptiveMember.newStatus(BasicDiagramStatus.TypeNameError,
                "typeName:" + getTypeName() + " name is invalid.");
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> vector = new Vector<>();
        if (statusOfMember().getValue0() != BasicDiagramStatus.Okay)
            vector.add(statusOfMember());
        return vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueType)) return false;
        ValueType valueType = (ValueType) o;
        return numberOfPointer == valueType.numberOfPointer && typeName.equals(valueType.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, numberOfPointer);
    }

    @Override
    public int compareTo(ValueType valueType) {
        if (valueType.getNumberOfPointer() != getNumberOfPointer())
            return getNumberOfPointer() - valueType.getNumberOfPointer();
        return getTypeName().compareTo(valueType.getTypeName());
    }

    @Override
    public String getShowName(String... className) {
        return  getTypeName() + "*".repeat(getNumberOfPointer());
    }

}
