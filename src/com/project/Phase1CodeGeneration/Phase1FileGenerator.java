package com.project.Phase1CodeGeneration;

import java.io.*;

public class Phase1FileGenerator {

    boolean successFull;
    public Phase1FileGenerator(CompleteDiagram diagram)
    {
        successFull = false;
        if(diagram.isSuccessCode())
        {
            try {
                File f = new File("phase1");
                f.mkdir();

                for(CompleteClass completeClass:diagram.getClasses())
                {
                        OutputStream fileOutputStream =
                                new FileOutputStream("phase1/" + completeClass.getName() + ".cpp");
                        fileOutputStream.write(generateClassCPP(completeClass).getBytes());
                }
                successFull = true;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public boolean isSuccessFull() {
        return successFull;
    }

    private String getBlock()
    {
        String base = "\n{\n";
        base = base + "\t\\\\TODO.yaml:code here\n";
        base = base + "}\n\n";
        return base;
    }

    private String generateMethod(String className, CompleteMethod method)
    {
        String baseClassName = method.getReturnValueType().getShowName() + " " + className + "::";
        return baseClassName +
                method.getShowName().substring(method.getReturnValueType().getShowName().length() + 1)
                + getBlock();
    }

    private String generateConstructor(String className, CompleteConstructor constructor)
    {
        String baseClassName = className + "::" + className;
        return baseClassName + constructor.getShowName() + getBlock();
    }

    private String generateDestructor(String className)
    {
        String baseClassName = className + "::" + "~" + className;
        return baseClassName + getBlock();
    }

    private String generateClassCPP(CompleteClass completeClass)
    {
        StringBuilder base = new StringBuilder();
        for(CompleteConstructor constructor:completeClass.getConstructors())
            base.append(generateConstructor(completeClass.getName(), constructor));
        for(CompleteMethod method:completeClass.getMethods())
            base.append(generateMethod(completeClass.getName(), method));
        if(completeClass.isHavingDestructor())
            base.append(generateDestructor(completeClass.getName()));
        return base.toString();
    }

}
