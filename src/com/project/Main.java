package com.project;

import com.project.classBaseUML.*;
import com.project.graphBaseDependency.GraphOperation;
import com.project.diagramGUI.*;
import com.project.phase1CodeGeneration.*;
import com.project.phase2CodeGeneration.DiagramInfo;
import com.project.phase2CodeGeneration.Phase2CodeGenerator;

import org.w3c.dom.*;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Thread.sleep;

public class Main implements Runnable{
    static public DocumentBuilderFactory documentFactory;
    static public DocumentBuilder documentBuilder;
    static public Document document;
    static private JFrame frame;
    static public Transformer transformer;
    static public GUIDiagram guiDiagram;
    static private int counter = 0;

    private static void generateInfoForXML(String fileName)
    {
        String xmlString;
        try {
            xmlString = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document generatedDocument = DescriptiveMember.getDocumentFromXML(xmlString);
        ClassDiagram diagram = new ClassDiagram();
        diagram.setDataByNode(generatedDocument.getDocumentElement());
        BasicDiagramStatus status = diagram.getStatusType();
        System.out.println("code:" + status.getBasicDiagramStatusCode());
        System.out.println("type:" + status);
        System.out.println(diagram.getAllProblems());

        if(diagram.getStatusType() == BasicDiagramStatus.Okay)
        {
            System.out.println();
            GraphOperation result = diagram.getResultOfGraphOperation();
            System.out.println("Dependency Number:" + result.getDependencyNumber());
            System.out.println(Arrays.toString(result.getAllCycles()));
            System.out.println();
            if(result.getDependencyNumber() != 0)
            {
                CompleteDiagram completeDiagram = new CompleteDiagram(diagram);
                Phase1CodeGenerator fileGenerator = new Phase1CodeGenerator(completeDiagram);
                System.out.println("generation success:" + fileGenerator.isSuccessFull());
//                DiagramInfo diagramInfo = new DiagramInfo("diagram_info");
//                if(diagramInfo.isSuccessful())
//                    System.out.println(diagramInfo);
            }
        }


    }

    public static void main(String[] args) {
        int x = 0;
        if(args[0].endsWith(".class") || args[0].endsWith(".jar"))
            x = 1;
        if(args.length < 2 || args[x].startsWith("-gui"))
            initializeGUI();
        else if(args[x].startsWith("-xml"))
            generateInfoForXML(args[x + 1]);
        else
        {
            String diagramInfoDirectory = "diagram_info";
            String phase1Directory = "phase1";
            String otherCFiles = "c_files";
            String headers = "headers";
            for(int i = x; i < args.length ; i += 2)
                switch (args[i])
                {
                    case "-i":
                    case "-info":
                    case "-I":
                        diagramInfoDirectory = args[i + 1];
                        break;
                    case "-p1":
                    case "-P1":
                    case "-phase1":
                        phase1Directory = args[i + 1];
                        break;
                    case "-c":
                    case "-C":
                        otherCFiles = args[i + 1];
                        break;
                    case "-h":
                    case "-H":
                    case "-headers":
                        headers = args[i + 1];
                        break;
                    default:
                        System.out.println(args[i] + " is illegal");
                        return;
                }
            Phase2CodeGenerator phase2 =
                    new Phase2CodeGenerator(diagramInfoDirectory, phase1Directory, otherCFiles, headers);

        }
    }

    public static void initializeGUI()
    {
        documentFactory = DocumentBuilderFactory.newInstance();
        TransformerFactory factory = TransformerFactory.newInstance();
        try {
            transformer = factory.newTransformer();
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
            return;
        }
        document = documentBuilder.newDocument();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridLayout a = new GridLayout(1,2);
        a.setVgap(50);
        frame.setLayout(a);

        guiDiagram = new GUIDiagram();
        frame.add(guiDiagram.getPanel());
        frame.setLocationRelativeTo(null);
        frame.setSize(1500, 750);

        frame.setVisible(true);
        frame.pack();
        new Thread(new Main()).start();

    }

    private static void loopOnGUI()
    {
        counter += 1;
        if(counter % 15 == 0)
        {
            BasicDiagramStatus status = guiDiagram.getStatusType();
            System.out.println("code:" + status.getBasicDiagramStatusCode());
            System.out.println("type:" + status);
            System.out.println(guiDiagram.getAllProblems());

            if(status == BasicDiagramStatus.Okay)
            {
                System.out.println();
                GraphOperation result = guiDiagram.getResultOfGraphOperation();
                System.out.println("Dependency Number:" + result.getDependencyNumber());
                System.out.println(Arrays.toString(result.getAllCycles()));
                System.out.println();
                if(counter %4 == 0 && result.getDependencyNumber() != 0)
                {
                    MethodOverloader.getOverloadTable().clear();
                    CompleteDiagram completeDiagram = new CompleteDiagram(guiDiagram);
                    Phase1CodeGenerator fileGenerator = new Phase1CodeGenerator(completeDiagram);
                    System.out.println("generation success:" + fileGenerator.isSuccessFull());
                    DiagramInfo diagramInfo = new DiagramInfo("diagram_info");
                    if(diagramInfo.isSuccessful())
                        System.out.println(diagramInfo);
                }
            }

            try {
                document = documentBuilder.newDocument();
                document.appendChild(guiDiagram.getElementDocument());
                String writer = DescriptiveMember.getXml(document);

                if(status == BasicDiagramStatus.Okay)
                    if(guiDiagram.getResultOfGraphOperation().getDependencyNumber() != 0)
                    {
                        OutputStream xmlOutputStream = new FileOutputStream("diagram_detail.xml");
                        xmlOutputStream.write(writer.getBytes());
                        xmlOutputStream.flush();
                        xmlOutputStream.close();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public void run() {
        do {
            try {
                sleep(1999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loopOnGUI();
            for (Component co : frame.getComponents())
                if (co instanceof JPanel)
                    ((JPanel) co).updateUI();
            frame.pack();
            frame.setMinimumSize(new Dimension(1500, 750));
        } while (true);
    }
}
