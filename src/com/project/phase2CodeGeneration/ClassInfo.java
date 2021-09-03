package com.project.phase2CodeGeneration;

import com.project.lexicalAnalyzer.TokenTypes;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.Vector;

public class ClassInfo {
    private String className;
    private boolean haveConstructor;
    private boolean haveDestructor;
    private boolean successful;
    private final HashSet<String> methods;
    private final HashSet<String> attributes;
    private final HashSet<String> parents;

    public ClassInfo(Vector<Pair<TokenTypes, String>> tokens) {

        parents = new HashSet<>();
        attributes = new HashSet<>();
        methods = new HashSet<>();
        successful = false;
        haveDestructor = false;
        haveConstructor = false;
        if(tokens == null)
            return;

        for (int i = 0; i < tokens.size(); i+=4)
        {
            Pair<TokenTypes, String> tokenType = tokens.get(i);
            Pair<TokenTypes, String> tokenValue = tokens.get(i + 2);

            switch (tokenType.getValue0())
            {
                case PARENT:
                    parents.add(tokenValue.getValue1());
                    break;
                case CONSTRUCTOR:
                    haveConstructor = !tokenValue.getValue1().equals("0");
                    break;
                case DESTRUCTOR:
                    haveDestructor = !tokenValue.getValue1().equals("0");
                    break;
                case METHOD:
                    methods.add(tokenValue.getValue1());
                    break;
                case ATTRIBUTE:
                    attributes.add(tokenValue.getValue1());
                    break;
                case CLASS:
                    className = tokenValue.getValue1();
                    break;
                default:
                    return;
            }
        }
        if(className == null)
            return;
        successful = true;
    }

    public String getClassName() {
        return className;
    }

    public boolean isHaveConstructor() {
        return haveConstructor;
    }

    public boolean isHaveDestructor() {
        return haveDestructor;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public HashSet<String> getMethods() {
        return methods;
    }

    public HashSet<String> getAttributes() {
        return attributes;
    }

    public HashSet<String> getParents() {
        return parents;
    }
}
