package com.project.phase1CodeGeneration;

import com.project.classBaseUML.*;
import org.javatuples.Pair;

import java.util.Vector;

public class CompleteClass extends
        ClassStructure<CompleteValueType, CompleteAttribute, CompleteConstructor, CompleteMethod> {

    private final boolean successCode;
    private final Vector<CompleteMethod> allMethods;
    private final Vector<Pair<String, CompleteMethod>> allMethodsBasedOnParents;
    private final Vector<CompleteAttribute> allAttributes;
    private final Vector<Pair<String, CompleteAttribute>> allAttributesBasedOnParents;
    private final Vector<String> parents;
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
            getMethods().add(new CompleteMethod((ClassMethod) method));
        for(Object constructor:structure.getConstructors())
            getConstructors().add(new CompleteConstructor((ClassConstructor) constructor));
        successCode = modifyByParents(getName(), diagram.getClasses(), 1);

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
                }

                for(Object method:classStructure.getMethods())
                {
                    CompleteMethod newMethod = new CompleteMethod((ClassMethod) method);
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

}
