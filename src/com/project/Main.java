package com.project;

import com.project.classBaseUML.*;
import com.project.graphBaseDependency.GraphOperation;
import com.project.diagramGUI.*;
import com.project.phase1CodeGeneration.CompleteDiagram;
import com.project.phase1CodeGeneration.Phase1CodeGenerator;
import com.project.lexicalAnalyzer.LexicalAnalyzer;
import com.project.lexicalAnalyzer.TokenTypes;
import org.javatuples.Pair;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;

public class Main implements Runnable{
    static public DocumentBuilderFactory documentFactory;
    static public DocumentBuilder documentBuilder;
    static public Document document;
    static private JFrame frame;
    static public Transformer transformer;
    static public GUIDiagram guiDiagram;

    private static void lexicalAnalyzerUser()
    {
        Vector<Pair<TokenTypes, String>> tokens = LexicalAnalyzer.getTokensOfPhase2Files("SearchEngine.c");
        FileWriter myWriter;
        try {
            myWriter = new FileWriter("tokens.txt");
            for(Pair<TokenTypes, String> token:tokens)
                myWriter.write(token.toString() + "\n");
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
//        lexicalAnalyzerUser();
        documentFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        document = documentBuilder.newDocument();

        DiagramGetter t = new DiagramGetter();
        t.init();

        ClassStructure<ValueType, ClassAttribute<ValueType>, ClassConstructor<ValueType, ClassAttribute<ValueType>>,
                ClassMethod<ValueType, ClassAttribute<ValueType>>> structure =
                new ClassStructure<>();
//        structure.getAttributes().add((ClassAttribute<ValueType>) attribute);
        frame = new JFrame();
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
        structure.setName("Biggy");
        document.appendChild(structure.getElementDocument());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return;
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();

        StreamResult streamResult = new StreamResult(writer);
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(writer);
        System.out.println(writer);

        try {
            documentFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(writer.toString())));
            System.out.println(document.getDocumentElement().getClass());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
    public String getXml(Document document)
    {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
            return "";
        }
        return writer.toString();
    }

    @Override
    public void run() {
        int counter = 0;
        do {
            try {
                sleep(1999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                }

                document = documentBuilder.newDocument();
                document.appendChild(guiDiagram.getElementDocument());
                try {
                    String writer = getXml(document);
                    documentFactory = DocumentBuilderFactory.newInstance();
                    documentBuilder = documentFactory.newDocumentBuilder();

                    System.out.println(writer);
                    document = documentBuilder.parse(new InputSource(new StringReader(writer)));

                    ClassDiagram diagram = new ClassDiagram();
                    diagram.setDataByNode(document.getDocumentElement());

                    System.out.println("diagram:");
                    document = documentBuilder.newDocument();
                    document.appendChild(diagram.getElementDocument());
                    System.out.println(getXml(document));
                    System.out.println("writer:");
                    System.out.println(writer);
                    System.out.println();
                } catch (SAXException | IOException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                if(counter %4 == 0)
                {
                    CompleteDiagram completeDiagram = new CompleteDiagram(guiDiagram);
                    Phase1CodeGenerator fileGenerator = new Phase1CodeGenerator(completeDiagram);
                    System.out.println("generation success:" + fileGenerator.isSuccessFull());
                }

            }
            for (Component co : frame.getComponents())
                if (co instanceof JPanel)
                    ((JPanel) co).updateUI();
            frame.pack();
            frame.setMinimumSize(new Dimension(1500, 750));
        } while (true);
    }
}
