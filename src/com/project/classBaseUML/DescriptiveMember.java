package com.project.classBaseUML;

import org.javatuples.Pair;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public interface DescriptiveMember {

    static <T extends DescriptiveMember> Pair<BasicDiagramStatus, LinkedList<String>> statusOfVector(
            Vector<T> vector, boolean checkSameSignature, String description, BasicDiagramStatus sameProblemStatus) {
        int numberOfParams = vector.size();
        for (int i = 0; i < numberOfParams; i++) {
            Pair<BasicDiagramStatus, LinkedList<String>> status = vector.get(i).statusOfMember();
            if (status.getValue0() != BasicDiagramStatus.Okay)
                return addDescriptionStatus(addDescriptionStatus(status, vector.get(i).getShowName()),
                        description + " #" + i + " :");
        }
        if (!checkSameSignature)
            return okStatus();
        for (int i = 0; i < numberOfParams; i++)
            for (int j = i + 1; j < numberOfParams; j++)
                if (vector.get(i).equals(vector.get(j)))
                    return newStatus(sameProblemStatus, description + " #" + i + " and #" + j + " are the same.");
        return okStatus();
    }

    static <T extends DescriptiveMember> Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblemsOfVector(
            Vector<T> vector, boolean checkSameSignature, String description, BasicDiagramStatus sameProblemStatus) {
        Vector<Pair<BasicDiagramStatus, LinkedList<String>>> allProblems = new Vector<>();
        int numberOfParams = vector.size();
        for (int i = 0; i < numberOfParams; i++)
            allProblems.addAll(addDescriptionStatus(vector.get(i).getAllProblems(),
                    description + " #" + i + " :"));
        if (!checkSameSignature)
            return allProblems;
        for (int i = 0; i < numberOfParams; i++)
            for (int j = i + 1; j < numberOfParams; j++)
                if (vector.get(i).equals(vector.get(j)))
                    allProblems.add(addDescriptionStatus(
                            newStatus(sameProblemStatus, vector.get(i).getShowName()),
                            description + " #" + i + " and #" + j + " are the same."));
        return allProblems;
    }

    static Pair<BasicDiagramStatus, LinkedList<String>> addDescriptionStatus(Pair<BasicDiagramStatus,
            LinkedList<String>> status, String description) {
        status.getValue1().add(0, description);
        return status;
    }

    static Vector<Pair<BasicDiagramStatus, LinkedList<String>>> addDescriptionStatus(
            Vector<Pair<BasicDiagramStatus, LinkedList<String>>> problems, String description) {
        for (Pair<BasicDiagramStatus, LinkedList<String>> pair : problems)
            addDescriptionStatus(pair, description);
        return problems;
    }

    static Pair<BasicDiagramStatus, LinkedList<String>> newStatus(BasicDiagramStatus status, String s) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(s);
        return new Pair<>(status, linkedList);
    }

    static Pair<BasicDiagramStatus, LinkedList<String>> okStatus() {
        return new Pair<>(BasicDiagramStatus.Okay, new LinkedList<>());
    }

    static String getXml(Document document)
    {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
            return "";
        }
        return writer.toString();
    }

    static Document getDocumentFromXML(String xmlString)
    {
        DocumentBuilderFactory ourDocumentFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder ourDocumentBuilder = ourDocumentFactory.newDocumentBuilder();
            return  ourDocumentBuilder.parse(new InputSource(new StringReader(xmlString)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    Element getElementDocument();

    static String generateParamsTogether (Vector<?> vector, String... optional)
    {
        StringBuilder returnValue = new StringBuilder();
        boolean moreThanOnce = false;
        returnValue.append(openParenthesis);
        if(optional.length > 0)
        {
            moreThanOnce = true;
            returnValue.append(optional[0]);
        }
        for (Object param : vector)
            if (moreThanOnce)
                returnValue.append(comma + whiteSpace).append(((DescriptiveMember)param).getShowName());
            else {
                moreThanOnce = true;
                returnValue.append(((DescriptiveMember)param).getShowName());
            }
        returnValue.append(closeParenthesis);
        return returnValue.toString();
    }

    void setDataByNode(Node node);

    BasicDiagramStatus getStatusType();

    Pair<BasicDiagramStatus, LinkedList<String>> statusOfMember();

    Vector<Pair<BasicDiagramStatus, LinkedList<String>>> getAllProblems();

    String getShowName(String... className);
}
