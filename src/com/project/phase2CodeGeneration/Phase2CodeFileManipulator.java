package com.project.phase2CodeGeneration;

import com.project.lexicalAnalyzer.CLanguageTokens;
import com.project.lexicalAnalyzer.LexicalAnalyzer;
import com.project.lexicalAnalyzer.TokenTypes;
import com.project.phase1CodeGeneration.CompleteAttribute;
import com.project.phase1CodeGeneration.Phase1CodeGenerator;
import org.javatuples.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public class Phase2CodeFileManipulator {

    enum FileType {
        C,H,CPP
    }

    enum LocationState {
        OUTSIDE, FUNCTION, METHOD, CONSTRUCTOR, DESTRUCTOR, STRUCTURE
    }

    enum LineState{
        NEW_LINE, RETURN_LINE,
    }

    private FileType fileType;
    private DiagramInfo diagramInfo;

    private Vector<Pair<TokenTypes,String>> pairVector;
    private Vector<Vector<CompleteAttribute>> variableStack;
    private Vector<Pair<CompleteAttribute,Vector<String>>> constructorStack;

    private LocationState locationState;
    private String locationClass;

    private LineState lineState;
    private String firstClassCalled;
    private String lastClassCalled;
    private int numberOfClassCalled;
    private int numberOfStructureKeywordCalled;
    private int numberOfClassKeywordCalled;
    private int numberOfTypeSpecifierCalled;
    private int numberOfTypeDetailAdded;
    private int numberOfIDCalled;
    private int depthOfParenthesis;

    private int depthOfCurlyBracket;
    private int depthOfSquareBracket;
    
    private FileWriter writer;

    public Phase2CodeFileManipulator(FileType type, Path path, DiagramInfo diagramInfo) {
        this.fileType = type;
        this.diagramInfo = diagramInfo;
        Vector<Pair<TokenTypes, String>>  tokens = LexicalAnalyzer.getTokensOfPhase2Files(path.normalize().toString());
        String valueOfPath = String.valueOf(path);
        if(type.equals(FileType.CPP))
            valueOfPath = valueOfPath.substring(0, valueOfPath.length() - 2);
        Path generatedPath = Path.of("generate/", valueOfPath);
        try {
            this.writer = new FileWriter(String.valueOf(generatedPath));
            generatePhase2(tokens);
            writer.flush();
            writer.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void write(String str)
    {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void printStringLine(String str,boolean addTab)
    {
        write(newLine);
        if(addTab)
            write(tab.repeat(depthOfCurlyBracket));
        write(str);
    }

    private void resetLineState()
    {
        lineState = LineState.NEW_LINE;
        firstClassCalled = null;
        lastClassCalled = null;
        numberOfClassCalled = 0;
        numberOfStructureKeywordCalled = 0;
        numberOfClassKeywordCalled = 0;
        numberOfTypeSpecifierCalled = 0;
        numberOfTypeDetailAdded = 0;
        numberOfIDCalled = 0;
    }

    private void flush()
    {
        // TODO add cache
        for(Pair<TokenTypes,String> token:pairVector) {
            write(token.getValue1());
        }
        pairVector.removeAllElements();
        switch (lineState) {
            case RETURN_LINE:
                printStringLine(closeCurlyBracket, true);
                break;
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resetLineState();

    }

    private void addStack()
    {
        this.variableStack.add(new Vector<>());
    }

    private void printStack()
    {
        for(CompleteAttribute attribute : this.variableStack.lastElement())
        {
            StringBuilder builder = new StringBuilder();
            builder.append(tab.repeat(Math.max(0,
                    this.depthOfCurlyBracket + (lineState.equals(LineState.RETURN_LINE)? 1:0))));

            if(diagramInfo.isHaveDestructor(attribute.getValueType().getTypeName()))
                builder.append(deleteKeyword);
            else
                builder.append(freeKeyword);

            builder.append(openParenthesis).append(and).append(attribute.getName())
                    .append(closeParenthesis).append(semiColon).append(newLine);
            write(builder.toString());
        }
    }

    private void removeStack()
    {
        printStack();
        variableStack.remove(variableStack.size() - 1);
    }

    private void decreaseParenthesis()
    {
        if(pairVector.lastElement().getValue0().equals(TokenTypes.ATTRIBUTE))
            pairVector.remove(pairVector.size() - 1);
        depthOfParenthesis--;
    }

    private void increaseParenthesis()
    {
        depthOfParenthesis++;
        if(depthOfParenthesis == 1)
            switch (locationState) {
                case OUTSIDE:
                    locationState = LocationState.FUNCTION;
                    break;
                case METHOD:
                case DESTRUCTOR:
                case CONSTRUCTOR:
                    pairVector.add(new Pair<>(TokenTypes.ATTRIBUTE,
                            unionKeyword + whiteSpace + locationClass + star + whiteSpace + thisManipulated));
                    pairVector.add(new Pair<>(TokenTypes.ATTRIBUTE, comma + whiteSpace));
            }
    }

    private void printfClosingCPPFunctions()
    {
        write(newLine);
        for (String method: diagramInfo.getMethods(locationClass))
            write(sharp + undefKeyword + whiteSpace + method + newLine);
        for (String attribute: diagramInfo.getAttributes(locationClass))
            write(sharp + undefKeyword + whiteSpace + attribute + newLine);
    }

    private void decreaseCurlyBracket()
    {
        flush();
        removeStack();
        depthOfCurlyBracket--;
        if(depthOfCurlyBracket == 0)
        {
            switch (locationState) {
                case METHOD:
                case DESTRUCTOR:
                case CONSTRUCTOR:
                    printfClosingCPPFunctions();
                    break;
            }
            locationState = LocationState.OUTSIDE;
        }
        else
        {

        }
    }

    private void printfOpeningCPPFunctions()
    {
        write(newLine);
        write(thisManipulated + arrow + thisManipulated + whiteSpace + equalSign + whiteSpace + thisManipulated);
        write(newLine);
        for (String method: diagramInfo.getMethods(locationClass))
            write(sharp + defineKeyword + whiteSpace +
                    method + openParenthesis + ellipsis + closeParenthesis + whiteSpace +
                    method + openParenthesis + thisManipulated + whiteSpace + comma + vaArgsToken + closeParenthesis
                    + newLine);
        for (String attribute: diagramInfo.getAttributes(locationClass))
            write(sharp + defineKeyword + whiteSpace +
                    attribute + whiteSpace + thisManipulated + arrow + attribute + newLine);
    }

    private void increaseCurlyBracket()
    {
        depthOfCurlyBracket++;
        flush();
        addStack();
        if(depthOfCurlyBracket == 1)
        {
            switch (locationState) {
                case OUTSIDE:
                    locationState = LocationState.STRUCTURE;
                    break;
                case METHOD:
                case DESTRUCTOR:
                case CONSTRUCTOR:
                    printfOpeningCPPFunctions();
                    break;
            }
        }
        else
        {

        }
    }

    private void decreaseSquareBracket()
    {
        depthOfSquareBracket --;
    }

    private void increaseSquareBracket()
    {
        depthOfSquareBracket ++;
    }

    private void addID()
    {
        numberOfIDCalled++;
    }

    private void typeDetailAdded()
    {
        numberOfTypeDetailAdded++;
    }

    private void typeSpecifierAdded()
    {
        numberOfTypeSpecifierCalled++;
    }

    private void destructorSignShowedUp()
    {
        locationClass = lastClassCalled;
        pairVector.removeElementAt(pairVector.size() - 1);
        pairVector.add(new Pair<>(TokenTypes.DESTRUCT, voidKeyword + whiteSpace + destructorKeyword));
        locationState = LocationState.DESTRUCTOR;
    }

    private void internalMethodShowedUp()
    {
        locationClass = lastClassCalled;
        pairVector.removeElementAt(pairVector.size() - 1);
        if(numberOfIDCalled > 0 || numberOfTypeSpecifierCalled - numberOfClassCalled > 0)
            locationState = LocationState.METHOD;
        else
        {
            pairVector.add(new Pair<>(TokenTypes.CONSTRUCTOR, voidKeyword + whiteSpace + constructorKeyword));
            locationState = LocationState.CONSTRUCTOR;
        }
    }

    private void dotCall()
    {

    }

    private void arrowCall()
    {

    }

    private void pointerAdded()
    {

    }

    private void referenceCall()
    {

    }

    private void classTypeCalled()
    {
//        lineState = LineState.NEW_LINE;

        if(numberOfClassCalled == 0)
            firstClassCalled = lastClassCalled;
        numberOfClassCalled ++;
        numberOfTypeSpecifierCalled ++;
    }

    private void classKeywordCalled()
    {
        numberOfStructureKeywordCalled++;
        numberOfClassKeywordCalled++;
    }

    private void generatePhase2(Vector<Pair<TokenTypes, String>> tokens)
    {
        this.depthOfParenthesis = 0;
        this.depthOfCurlyBracket = 0;
        this.depthOfSquareBracket = 0;
        this.locationState = LocationState.OUTSIDE;
        resetLineState();
        this.pairVector = new Vector<>();
        this.variableStack = new Vector<>();
        this.constructorStack = new Vector<>();

        for(Pair<TokenTypes, String> token:tokens)
            switch (token.getValue0())
            {
                case OPEN_PARENTHESIS:
                    pairVector.add(token);
                    increaseParenthesis();
                    break;
                case CLOSE_PARENTHESIS:
                    decreaseParenthesis();
                    pairVector.add(token);
                    break;
                case OPEN_CURLY_BRACKET:
                    pairVector.add(token);
                    increaseCurlyBracket();
                    break;
                case CLOSE_CURLY_BRACKET:
                    decreaseCurlyBracket();
                    pairVector.add(token);
                    flush();
                    break;
                case OPEN_SQUARE_BRACKET:
                    pairVector.add(token);
                    increaseSquareBracket();
                    break;
                case CLOSE_SQUARE_BRACKET:
                    pairVector.add(token);
                    decreaseSquareBracket();
                    break;
                case EMPTY_STRING:
                case COMMENT:
                    if(pairVector.size() ==0 || !pairVector.lastElement().getValue0().equals(TokenTypes.NEW))
                        pairVector.add(new Pair<>(TokenTypes.IGNORE, token.getValue1()));
                    break;
                case ID:
                case PARENT:
                case CONSTRUCTOR:
                case DESTRUCTOR:
                case METHOD:
                case ATTRIBUTE:
                    if(diagramInfo.getClassNames().contains(token.getValue1()))
                    {
                        pairVector.add(new Pair<>(TokenTypes.CLASS_TYPE, token.getValue1()));
                        lastClassCalled = token.getValue1();
                        classTypeCalled();
                    }
                    else
                    {
                        pairVector.add(new Pair<>(TokenTypes.ID, token.getValue1()));
                        addID();
                    }
                break;
                case THIS:
                    pairVector.add(new Pair<>(TokenTypes.ID, thisManipulated));
                    addID();
                    break;
                case CONST:
                case VOLATILE:
                case EXTERN:
                case TYPEDEF:
                case STATIC:
                case AUTO:
                case REGISTER:
                case SIGN:
                    pairVector.add(new Pair<>(TokenTypes.TYPE_DETAIL, token.getValue1()));
                    typeDetailAdded();
                    break;
                case OPERATOR_OR_ASSIGN:
                case STRING:
                case NUMBER:
                case ELLIPSIS:
                case TILDA:
                case COMMA:
                case MALLOC:
                case DELETE:
                case SIZEOF:
                    pairVector.add(new Pair<>(TokenTypes.SEPARATOR, token.getValue1()));
                    break;
                case NEW:
                    pairVector.add(token);
                    break;
                case DOUBLE_COLON:
                    internalMethodShowedUp();
                    break;
                case DOT:
                    pairVector.add(token);
                    dotCall();
                    break;
                case ARROW:
                    pairVector.add(token);
                    arrowCall();
                    break;
                case DESTRUCT:
                    destructorSignShowedUp();
                    break;
                case STAR:
                    pairVector.add(token);
                    pointerAdded();
                    break;
                case REFERENCE:
                    pairVector.add(token);
                    referenceCall();
                    break;
                case STRUCT:
                case UNION:
                case TYPES:
                case ENUM:
                    pairVector.add(new Pair<>(TokenTypes.TYPE_SPECIFIER, token.getValue1()));
                    typeSpecifierAdded();
                    break;
                case CLASS:
                    pairVector.add(new Pair<>(TokenTypes.CLASS, CLanguageTokens.unionKeyword));
                    classKeywordCalled();
                    break;
                case UNKNOWN:
                case MACRO:
                case SEMI_COLON:
                case COLON:
                case RESERVED_KEYWORD:
                    pairVector.add(token);
                    flush();
                    break;
                case RETURN:
                    printStringLine(openCurlyBracket, true);
                    printStack();
                    pairVector.add(token);
                    lineState = LineState.RETURN_LINE;
                    break;
                default:
                    break;
            }
        flush();
    }
}
