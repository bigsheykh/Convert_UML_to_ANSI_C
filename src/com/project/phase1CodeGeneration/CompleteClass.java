package com.project.phase1CodeGeneration;

import com.project.classBaseUML.*;
import org.javatuples.Pair;

import java.util.Vector;

public class CompleteClass extends
        ClassStructure<CompleteValueType, CompleteAttribute, CompleteConstructor, CompleteMethod> {

    private final boolean successCode;
    private Vector<Pair<String, CompleteMethod>> allMethods;
    private Vector<Pair<String, CompleteAttribute>> allAttributes;
    private Vector<String> parents;
    public CompleteClass(ClassStructure structure, ClassDiagram diagram) {
        super();
        allMethods = new Vector<>();
        allAttributes = new Vector<>();
        parents = new Vector<>();
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
                parents.add(className);
                for(Object attribute:classStructure.getAttributes())
                {
                    CompleteAttribute newAttribute = new CompleteAttribute((ClassAttribute) attribute);
                    getAttributes().add(newAttribute);
                    allAttributes.add(new Pair<>(className, newAttribute));
                }

                for(Object method:classStructure.getMethods())
                    allMethods.add(new Pair<>(className, new CompleteMethod((ClassMethod) method)));

                return modifyByParents(classStructure.getSuperClass(), diagram, counter + 1);
            }
        }
        return false;
    }

    public boolean isSuccessCode() {
        return successCode;
    }

}
