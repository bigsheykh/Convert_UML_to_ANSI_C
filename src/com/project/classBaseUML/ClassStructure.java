package com.project.classBaseUML;

import com.project.lexicalAnalyzer.LexicalAnalyzer;
import com.project.graphBaseDependency.DependencyEdge;
import com.project.graphBaseDependency.DependencyStatus;
import org.javatuples.Pair;
import org.w3c.dom.*;

import java.util.*;

import static com.project.Main.document;

public class ClassStructure<TType extends ValueType, TAttribute extends ClassAttribute<TType>
        , TConstructor extends ClassConstructor<TType, TAttribute>,
        TMethod extends ClassMethod<TType, TAttribute>> implements DescriptiveMember {
    private final Vector<TConstructor> constructors = new Vector<>();
    private final Vector<TAttribute> attributes = new Vector<>();
    private final Vector<TMethod> methods = new Vector<>();
    private boolean havingDestructor = false;
    private String superClass = "null";
    private String name;

    public ClassStructure(String name) {
        this.name = name;
    }

    public ClassStructure() {
    }

    Vector <DependencyEdge> getAllAttributeDependenciesEdges() {
        Vector<DependencyEdge> allEdges = new Vector<>();
        for(TAttribute attribute:getAttributes())
            if(attribute.getValueType().getNumberOfPointer() == 0)
                allEdges.add(new DependencyEdge(
                        DependencyStatus.ObjectAttribute, getName(),attribute.getValueType().getTypeName()));
            else
                allEdges.add(new DependencyEdge(
                        DependencyStatus.PointerAttribute, getName(),attribute.getValueType().getTypeName()));
        return allEdges;
    }

    Vector <DependencyEdge> getAllMethodDependenciesEdges() {
        Vector<DependencyEdge> allEdges = new Vector<>();
        Set<String> allParamTypesWithoutPointer = new HashSet<>();
        Set<String> allParamTypesWithPointer = new HashSet<>();

        for(TMethod method:getMethods())
        {
            if(method.getReturnValueType().getNumberOfPointer() == 0)
                allEdges.add(new DependencyEdge(
                        DependencyStatus.MethodObjectType, method.getReturnValueType().getTypeName(), getName()));
            else
                allEdges.add(new DependencyEdge(
                        DependencyStatus.MethodPointerType, method.getReturnValueType().getTypeName(), getName()));
            allParamTypesWithPointer.addAll(method.allParamTypesWithPointer());
            allParamTypesWithoutPointer.addAll(method.allParamTypesWithoutPointer());

        }

        for(String className:allParamTypesWithoutPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.MethodObjectParameter, getName(), className));
        for(String className:allParamTypesWithPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.MethodPointerParameter, getName(), className));

        return allEdges;
    }

    Vector <DependencyEdge> getAllConstructorDependenciesEdges() {
        if(getConstructors().size() == 0)
            return new Vector<>();
        Vector<DependencyEdge> allEdges = new Vector<>();

        Set<String> allParamTypesWithoutPointer = new HashSet<>();
        Set<String> allParamTypesWithPointer = new HashSet<>();
        Set<String> intersectionOfAllParamTypesWithoutPointer = getConstructors().get(0).allParamTypesWithoutPointer();
        Set<String> intersectionOfAllParamTypesWithPointer = getConstructors().get(0).allParamTypesWithPointer();
        Set<String> intersectionOfAllParamTypes = getConstructors().get(0).allParamTypes();

        for(TConstructor constructor:getConstructors())
        {
            allParamTypesWithPointer.addAll(constructor.allParamTypesWithPointer());
            allParamTypesWithoutPointer.addAll(constructor.allParamTypesWithoutPointer());
            intersectionOfAllParamTypesWithoutPointer.retainAll(constructor.allParamTypesWithoutPointer());
            intersectionOfAllParamTypesWithPointer.retainAll(constructor.allParamTypesWithPointer());
            intersectionOfAllParamTypes.retainAll(constructor.allParamTypes());
        }

        for(String className:allParamTypesWithoutPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.ConstructorObjectParameter, getName(), className));
        for(String className:allParamTypesWithPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.ConstructorPointerParameter, getName(), className));
        for(String className:intersectionOfAllParamTypes)
            allEdges.add(new DependencyEdge(DependencyStatus.ConstructorAllHybridParameter, getName(), className));
        for(String className:intersectionOfAllParamTypesWithoutPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.ConstructorAllObjectParameter, getName(), className));
        for(String className:intersectionOfAllParamTypesWithPointer)
            allEdges.add(new DependencyEdge(DependencyStatus.ConstructorAllPointerParameter, getName(), className));

        return allEdges;
    }

    Vector <DependencyEdge> getAllDependenciesEdges()
    {
        Vector<DependencyEdge> allEdges = new Vector<>();
        allEdges.add(new DependencyEdge(DependencyStatus.SuperClass,getName(),getSuperClass()));
        allEdges.addAll(getAllAttributeDependenciesEdges());
        allEdges.addAll(getAllConstructorDependenciesEdges());
        allEdges.addAll(getAllMethodDependenciesEdges());
        return allEdges;
    }

    public void unsetSuperClass() {
        superClass = "null";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<TConstructor> getConstructors() {
        return constructors;
    }

    public Vector<TAttribute> getAttributes() {
        return attributes;
    }

    public Vector<TMethod> getMethods() {
        return methods;
    }

    public boolean isHavingDestructor() {
        return havingDestructor;
    }

    public void setHavingDestructor(boolean havingDestructor) {
        this.havingDestructor = havingDestructor;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public Element getElementDocument() {
        Element root = document.createElement("Class");

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(getName()));
        root.appendChild(name);

        Element superClass = document.createElement("super");
        superClass.appendChild(document.createTextNode(getSuperClass()));
        root.appendChild(superClass);

        Element havingDestructor = document.createElement("destructor");
        havingDestructor.appendChild(document.createTextNode(String.valueOf(isHavingDestructor())));
        root.appendChild(havingDestructor);

        Element allAttributes = document.createElement("attributes");
        for (TAttribute param : getAttributes())
            allAttributes.appendChild(param.getElementDocument());
        Element allConstructors = document.createElement("constructors");

        for (TConstructor param : getConstructors())
            allConstructors.appendChild(param.getElementDocument());
        Element allMethods = document.createElement("methods");

        for (TMethod param : getMethods())
            allMethods.appendChild(param.getElementDocument());

        root.appendChild(allAttributes);
        root.appendChild(allConstructors);
        root.appendChild(allMethods);
        return root;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassStructure)) return false;
        ClassStructure<?, ?, ?, ?> structure = (ClassStructure<?, ?, ?, ?>) o;
        return name.equals(structure.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public BasicDiagramStatus getStatusType() {
        return statusOfMember().getValue0();
    }

    @Override
    public Pair<BasicDiagramStatus, LinkedList<String>> statusOfMember() {
        if (!LexicalAnalyzer.isNameOkayInC(getSuperClass()))
            return DescriptiveMember.newStatus(BasicDiagramStatus.SuperClassNameError, "super name is invalid");
        if (!LexicalAnalyzer.isNameOkayInC(getName()) || getName().equals("null"))
            return DescriptiveMember.newStatus(BasicDiagramStatus.ClassNameError, "class name is invalid");

        Pair<BasicDiagramStatus, LinkedList<String>> status = DescriptiveMember.statusOfVector(
                getConstructors(), true, "Constructor", BasicDiagramStatus.SameConstructor);
        if (status.getValue0() != BasicDiagramStatus.Okay)
            return status;
        status = DescriptiveMember.statusOfVector(
                getAttributes(), true, "Attributes", BasicDiagramStatus.SameAttributeName);
        if (status.getValue0() != BasicDiagramStatus.Okay)
            return status;

        return DescriptiveMember.statusOfVector(
                getMethods(), true, "methods", BasicDiagramStatus.SameMethodSignature);
    }

    @Override
    public Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems() {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> vector = new Vector<>();
        if (!LexicalAnalyzer.isNameOkayInC(getSuperClass()))
            vector.add(DescriptiveMember.newStatus(BasicDiagramStatus.SuperClassNameError, "super name is invalid"));
        if (!LexicalAnalyzer.isNameOkayInC(getName()) || getName().equals("null"))
            vector.add(DescriptiveMember.newStatus(BasicDiagramStatus.ClassNameError, "class name is invalid"));

        vector.addAll(DescriptiveMember.getAllProblemsOfVector(
                getConstructors(), true, "Constructor", BasicDiagramStatus.SameConstructor));
        vector.addAll(DescriptiveMember.getAllProblemsOfVector(
                getAttributes(), true, "Attributes", BasicDiagramStatus.SameAttributeName));
        vector.addAll(DescriptiveMember.getAllProblemsOfVector(
                getMethods(), true, "methods", BasicDiagramStatus.SameMethodSignature));

        return vector;
    }

    public boolean havingSuperClass() {
        return !getSuperClass().equals("null");
    }

    @Override
    public String getShowName(String... className) {
        return getName();
    }

    public void setDataByNode(Node node) {
        unsetSuperClass();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            NodeList allNodesList;
            switch (currentNode.getNodeName()) {
                case "name":
                    if (currentNode.getFirstChild() != null)
                        setName(currentNode.getFirstChild().getNodeValue());
                    break;
                case "super":
                    if (currentNode.getFirstChild() != null)
                        setSuperClass(currentNode.getFirstChild().getNodeValue());
                    break;
                case "destructor":
                    if (currentNode.getFirstChild() != null)
                        setHavingDestructor(currentNode.getFirstChild().getNodeValue().equals("true"));
                    break;
                case "constructors":
                    allNodesList = currentNode.getChildNodes();
                    for (int j = 0; j < allNodesList.getLength(); j++) {
                        Node constructorNode = allNodesList.item(j);
                        if (constructorNode.getNodeName().equals("Constructor")) {
                            TConstructor constructor = (TConstructor) new ClassConstructor<TType, TAttribute>();
                            constructor.setDataByNode(constructorNode);
                            getConstructors().add(constructor);
                        }
                    }
                    break;
                case "attributes":
                    allNodesList = currentNode.getChildNodes();
                    for (int j = 0; j < allNodesList.getLength(); j++) {
                        Node attributeNode = allNodesList.item(j);
                        if (attributeNode.getNodeName().equals("Attribute")) {
                            TAttribute attribute = (TAttribute) new ClassAttribute<TType>();
                            attribute.setDataByNode(attributeNode);
                            getAttributes().add(attribute);
                        }
                    }
                    break;
                case "methods":
                    allNodesList = currentNode.getChildNodes();
                    for (int j = 0; j < allNodesList.getLength(); j++) {
                        Node methodNode = allNodesList.item(j);
                        if (methodNode.getNodeName().equals("Method")) {
                            TMethod method = (TMethod) new ClassMethod<TType, TAttribute>();
                            method.setDataByNode(methodNode);
                            getMethods().add(method);
                        }
                    }
                    break;
                default:
                    System.out.println("Method:" + currentNode.getNodeName());
                    break;
            }

        }

    }

}
