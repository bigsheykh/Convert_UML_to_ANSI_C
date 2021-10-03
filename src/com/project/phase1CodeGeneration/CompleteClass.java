package com.project.phase1CodeGeneration;

import com.project.classBaseUML.*;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.Vector;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public class CompleteClass extends
        ClassStructure<CompleteValueType, CompleteAttribute, CompleteConstructor, CompleteMethod> {

    private final boolean successCode;
    private final Vector<CompleteMethod> allMethods;
    private final Vector<Pair<String, CompleteMethod>> allMethodsBasedOnParents;
    private final Vector<CompleteAttribute> allAttributes;
    private final Vector<Pair<String, CompleteAttribute>> allAttributesBasedOnParents;
    private final Vector<String> parents;
    private final String hashAdded;

    public CompleteClass(ClassStructure structure, ClassDiagram diagram) {
        super();
        allMethods = new Vector<>();
        allAttributes = new Vector<>();
        parents = new Vector<>();
        allAttributesBasedOnParents = new Vector<>();
        allMethodsBasedOnParents = new Vector<>();
        setName(structure.getName());
        setSuperClass(structure.getSuperClass());
        setHavingDestructor(structure.isHavingDestructor());
        for(Object method:structure.getMethods())
            getMethods().add(new CompleteMethod((ClassMethod) method, getName()));
        for(Object constructor:structure.getConstructors())
            getConstructors().add(new CompleteConstructor((ClassConstructor) constructor, getName()));
        successCode = modifyByParents(getName(), diagram.getClasses(), 1);
        hashAdded = MethodOverloader.randomGenerator();
        MethodOverloader.addToTable(deleteManipulate, mangledDeleteName(), getName(), new Vector<>());
    }

    public String getPhase2Info()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(classKeyword + colon).append(getName()).append(newLine);
        for(String parentName:getParents())
            if(!parentName.equals(getName()))
                builder.append(parentKeyword + colon).append(parentName).append(newLine);

        if(getConstructors().size() == 0)
            builder.append(constructorKeyword + colon).append(0).append(newLine);
        else
            builder.append(constructorKeyword + colon).append(1).append(newLine);

        if(isHavingDestructor())
            builder.append(destructorKeyword + colon).append(1).append(newLine);
        else
            builder.append(destructorKeyword + colon).append(0).append(newLine);

        for(String methodName: getAllMethodsName())
            builder.append(methodKeyword + colon).append(methodName).append(newLine);

        for(CompleteAttribute completeAttribute:getAllAttributes())
            builder.append(attributeKeyword + colon).append(completeAttribute.getName()).append(newLine);
        return builder.toString();
    }

    private boolean modifyByParents(String className, Vector diagram, int counter) {
        if(className.equals("null"))
            return true;
        if(counter > diagram.size())
            return false;
        for (int i = 0; i < diagram.size(); i++) {
            ClassStructure classStructure = (ClassStructure) diagram.get(i);
            if(classStructure.getName().equals(className))
            {
                boolean resultOfParents = modifyByParents(classStructure.getSuperClass(), diagram, counter + 1);
                if(!resultOfParents)
                    return false;
                parents.add(className);
                for(Object attribute:classStructure.getAttributes())
                {
                    CompleteAttribute newAttribute = new CompleteAttribute((ClassAttribute) attribute);
                    getAttributes().add(newAttribute); //TODO : decide to keep this line or not
                    allAttributesBasedOnParents.add(new Pair<>(className, newAttribute));
                    allAttributes.add(newAttribute);
                    if(!className.equals(getName()))
                        getAttributes().add(newAttribute);
                }

                for(Object method:classStructure.getMethods())
                {
                    CompleteMethod newMethod = new CompleteMethod((ClassMethod) method, getName());
                    getAllMethodsBasedOnParents().
                            removeIf(completeMethod -> completeMethod.getValue1().equals(newMethod));
                    allMethodsBasedOnParents.add(new Pair<>(className, newMethod));
                    allMethods.add(newMethod);
                }

                return true;
            }
        }
        return false;
    }

    public Vector<CompleteMethod> getAllMethods() {
        return allMethods;
    }

    public Vector<Pair<String, CompleteMethod>> getAllMethodsBasedOnParents() {
        return allMethodsBasedOnParents;
    }

    public Vector<CompleteAttribute> getAllAttributes() {
        return allAttributes;
    }

    public Vector<Pair<String, CompleteAttribute>> getAllAttributesBasedOnParents() {
        return allAttributesBasedOnParents;
    }

    public Vector<String> getParents() {
        return parents;
    }

    public boolean isSuccessCode() {
        return successCode;
    }

    public HashSet<String> getAllMethodsName()
    {
        HashSet<String> names = new HashSet<>();
        for(CompleteMethod completeMethod:getAllMethods())
            names.add(completeMethod.getName());
        return names;
    }

    public static String generateCastUse(String name,String parentName)
    {
        return name + dot + parentName;
    }

    private String mangledDeleteName()
    {
        return deleteKeyword + hashAdded;
    }
    public String generateDeleteDefinition()
    {
        StringBuilder allLines = new StringBuilder();
        allLines.append(voidKeyword + whiteSpace + mangledDeleteName())
                .append(DescriptiveMember.generateParamsTogether(
                new Vector<>(), CompleteAttribute.generateAttributeThisText(getName())));
        allLines.append(semiColon).append(newLine);
        return allLines.toString();
    }

    String generateDeleteBody() {
        StringBuilder allLines = new StringBuilder();
        allLines.append(voidKeyword + whiteSpace + mangledDeleteName())
                .append(DescriptiveMember.generateParamsTogether(
                new Vector<>(), CompleteAttribute.generateAttributeThisText(getName())));
        allLines.append(newLine + openCurlyBracket + newLine);
        allLines.append(tab).append(generateDestructorName())
                .append(openParenthesis + thisKeyword + closeParenthesis + semiColon + newLine);
//        allLines.append(tab + freeKeyword + openParenthesis).append(thisKeyword).append(closeParenthesis)
//                .append(semiColon).append(newLine);
        allLines.append(closeCurlyBracket).append(newLine);

        return allLines.toString();
    }

    public String generateDestructorName()
    {
        return destructorKeyword + getName();
    }

    public String generateDestructorDefinition()
    {
        return voidKeyword + whiteSpace + generateDestructorName() +
                DescriptiveMember.generateParamsTogether(new Vector<>()
                        , CompleteAttribute.generateAttributeThisText(getName())) + semiColon + newLine;
    }

}
