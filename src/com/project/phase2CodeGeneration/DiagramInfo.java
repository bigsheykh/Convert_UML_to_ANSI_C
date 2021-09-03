package com.project.phase2CodeGeneration;

import com.project.lexicalAnalyzer.LexicalAnalyzer;
import com.project.lexicalAnalyzer.TokenTypes;
import org.javatuples.Pair;

import java.util.HashSet;
import java.util.Vector;

public class DiagramInfo {
    private final Vector<ClassInfo> infoVector;
    private final HashSet<String> classNames;
    private boolean successful;

    public DiagramInfo(String directory) {
        successful = false;
        classNames = new HashSet<>();
        infoVector = new Vector<>();
        if(!directory.endsWith("/"))
            directory = directory.concat("/");
        Vector<Pair<TokenTypes, String>> classes =
                LexicalAnalyzer.getTokensOfPhase2Files(directory + "AllClasses.info");
        if(classes == null)
            return;
        for (Pair<TokenTypes, String> token:classes)
            if(token.getValue0().equals(TokenTypes.ID))
                classNames.add(token.getValue1());
        for(String className:classNames)
        {
            ClassInfo classInfo =
                    new ClassInfo(LexicalAnalyzer.getTokensOfPhase2Files(directory + className + ".info"));
            if(!classInfo.isSuccessful())
                return;
            infoVector.add(classInfo);
        }
        successful = true;
    }

    public Vector<ClassInfo> getInfoVector() {
        return infoVector;
    }

    public HashSet<String> getClassNames() {
        return classNames;
    }

    public boolean isSuccessful() {
        return successful;
    }

}
