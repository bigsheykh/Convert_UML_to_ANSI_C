package com.project.phase2CodeGeneration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class Phase2CodeGenerator {

    boolean successful;
    private DiagramInfo diagramInfo;

    private final Vector<Phase2CodeFileManipulator> files;

    private void addPath(Phase2CodeFileManipulator.FileType type, Path path)
    {
        Phase2CodeFileManipulator fileManipulator = new Phase2CodeFileManipulator(type, path, diagramInfo);
        files.add(fileManipulator);
        //TODO other instructions
    }

    public Phase2CodeGenerator(String diagramInfoDirectory, String phase1Directory, String otherCFiles, String headers)
    {
        this.files = new Vector<>();
        successful = false;
        diagramInfo = new DiagramInfo(diagramInfoDirectory);
        if(!diagramInfo.isSuccessful())
        {
            System.out.println("diagram info wasn't successful");
            return;
        }
        try {
            Files.find(Paths.get(headers),Integer.MAX_VALUE,
                            (filePath, fileAttr) -> fileAttr.isRegularFile() && (filePath.endsWith(".h")))
                    .forEach(item -> addPath(Phase2CodeFileManipulator.FileType.H, item));

            Files.find(Paths.get(otherCFiles), Integer.MAX_VALUE,
                            (filePath, fileAttr) -> fileAttr.isRegularFile() &&
                                    (filePath.endsWith(".c") || filePath.endsWith(".h")))
                    .forEach(item -> addPath(Phase2CodeFileManipulator.FileType.C ,item));

            Files.find(Paths.get(phase1Directory),Integer.MAX_VALUE
                    ,(filePath, fileAttr) -> fileAttr.isRegularFile() && (filePath.endsWith(".cpp")))
                    .forEach(item -> addPath(Phase2CodeFileManipulator.FileType.CPP, item));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isSuccessful() {
        return successful;
    }
}
