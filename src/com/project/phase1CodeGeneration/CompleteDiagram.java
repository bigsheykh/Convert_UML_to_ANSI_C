package com.project.phase1CodeGeneration;

import com.project.classBaseUML.BasicDiagramStatus;
import com.project.classBaseUML.ClassDiagram;
import com.project.classBaseUML.ClassStructure;

public class CompleteDiagram extends
        ClassDiagram<CompleteValueType, CompleteAttribute, CompleteConstructor, CompleteMethod, CompleteClass> {
    private boolean successCode;
    public CompleteDiagram(ClassDiagram diagram) {
        super();
        successCode = false;
        if(diagram.getStatusType() != BasicDiagramStatus.Okay ||
                diagram.getResultOfGraphOperation().getDependencyNumber() == 0)
            return;

        for(Object structure:diagram.getClasses())
            getClasses().add(new CompleteClass((ClassStructure) structure, diagram));

        if(getStatusType() == BasicDiagramStatus.Okay ||
                getResultOfGraphOperation().getDependencyNumber() != 0)
            successCode = true;
        for(CompleteClass completeClass:getClasses())
            successCode = successCode && completeClass.isSuccessCode();
    }

    public boolean isSuccessCode() {
        return successCode;
    }
}
