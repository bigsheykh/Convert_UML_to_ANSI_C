package com.project.phase2CodeGeneration;

import com.project.classBaseUML.ClassAttribute;
import com.project.classBaseUML.ValueType;
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
        NEW_LINE, RETURN_LINE, POSSIBLY_INITIALIZER, DEFINITELY_NOT_A_INITIALIZER
    }

    private final FileType fileType;
    private final DiagramInfo diagramInfo;

    private Vector<Pair<TokenTypes,String>> pairVector;
    private Vector<Vector<CompleteAttribute>> variableStack;

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

    private FileWriter writer;

    public Phase2CodeFileManipulator(FileType type, Path path, DiagramInfo diagramInfo) {
        this.fileType = type;
        this.diagramInfo = diagramInfo;
        Vector<Pair<TokenTypes, String>>  tokens = LexicalAnalyzer.getTokensOfPhase2Files(path.normalize().toString());
        if(tokens == null)
            return;
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
        if(lineState.equals(LineState.POSSIBLY_INITIALIZER))
            initializersHandler();
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

    private Vector<Vector<Pair<TokenTypes,String>>> separateByComma(int begin, int end)
    {
        Vector<Vector<Pair<TokenTypes,String>>> returnValue = new Vector<>();
        Vector<Pair<TokenTypes,String>> cache = new Vector<>();
        int prBalance = 0;

        for (int i = begin; i < end;i++)
            if (pairVector.elementAt(i).getValue0().equals(TokenTypes.COMMA) && prBalance == 0)
            {
                returnValue.add(cache);
                cache = new Vector<>();
            }
            else
            {
                cache.add(pairVector.elementAt(i));
                if(pairVector.elementAt(i).getValue0().equals(TokenTypes.OPEN_PARENTHESIS))
                    prBalance++;
                else if(pairVector.elementAt(i).getValue0().equals(TokenTypes.CLOSE_PARENTHESIS))
                    prBalance--;
            }
        returnValue.add(cache);
        return returnValue;
    }

    private Vector<Pair<TokenTypes,String>> manipulateForInitialize(Vector<Pair<TokenTypes,String>> theVector)
    {
        String id = "";
        for(int i = 0; i < theVector.size();i++)
            if(theVector.get(i).getValue0().equals(TokenTypes.OPEN_PARENTHESIS))
            {
                variableStack.lastElement().add(new CompleteAttribute(new ClassAttribute<>(
                    new ValueType(firstClassCalled, 0), id)));
                theVector.add(i, new Pair<>(TokenTypes.OPERATOR_OR_ASSIGN,
                        whiteSpace + equalSign + whiteSpace +
                                star + generateNewName(firstClassCalled)));
                break;
            }
            else if(theVector.get(i).getValue0().equals(TokenTypes.ID))
                id = theVector.get(i).getValue1();
        return theVector;
    }

    private String generateNewName(String className) {
        return newKeyword + className;
    }

    private void initializersHandler()
    {
        Vector<Vector<Pair<TokenTypes,String>>> separated = separateByComma(0, pairVector.size());
        boolean foundClass = false;
        for(Pair<TokenTypes, String> pair:separated.elementAt(0))
            if (!foundClass)
                switch (pair.getValue0())
                {
                    case OPEN_PARENTHESIS:
                    case OPERATOR_OR_ASSIGN:
                    case ID:
                    case THIS:
                    case STAR:
                    case NEW:
                    case ARROW:
                    case SEMI_COLON:
                        return;
                    case CLASS_TYPE:
                        foundClass = true;
                        break;
                }

        pairVector = new Vector<>();
        for (int i = 0 ; i < separated.size();i++)
        {
            if(i != 0)
                pairVector.add(new Pair<>(TokenTypes.COMMA, comma));
            pairVector.addAll(manipulateForInitialize(separated.elementAt(i)));
        }
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
                builder.append(deleteManipulate);
//            else
//                builder.append(freeKeyword);

            if(diagramInfo.isHaveDestructor(attribute.getValueType().getTypeName()))
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
            popLastPairVectorElement();
        depthOfParenthesis--;
    }

    private void increaseParenthesis()
    {
        depthOfParenthesis++;
        if(depthOfCurlyBracket == 0)
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
        else if(pairVector.size() > 2 &&
                pairVector.elementAt(pairVector.size() - 3).getValue0().equals(TokenTypes.DOT))
            methodCallHandler(false);
        else if(pairVector.size() > 2 &&
                pairVector.elementAt(pairVector.size() - 3).getValue0().equals(TokenTypes.ARROW))
            methodCallHandler(true);
    }

    private void printfClosingCPPFunctions()
    {
        write(newLine);
//        for (String method: diagramInfo.getMethods(locationClass))
//            write(sharp + undefKeyword + whiteSpace + method + newLine);
//        for (String attribute: diagramInfo.getAttributes(locationClass))
//            write(sharp + undefKeyword + whiteSpace + attribute + newLine);
    }

    private int findMethodCallerBlock()
    {
        int prBalance = 0;
        int tokenPalace = pairVector.size() - 1;
        while (tokenPalace > -1 && (!pairVector.elementAt(tokenPalace).getValue0().equals(TokenTypes.ID) ||
                pairVector.elementAt(tokenPalace -1).getValue0().equals(TokenTypes.ARROW) ||
                pairVector.elementAt(tokenPalace -1).getValue0().equals(TokenTypes.DOT)))
        {
            if(pairVector.elementAt(tokenPalace).getValue0().equals(TokenTypes.CLOSE_PARENTHESIS))
                prBalance = 1;
            tokenPalace --;
            while (tokenPalace > -1 && prBalance !=0)
            {
                if(pairVector.elementAt(tokenPalace).getValue0().equals(TokenTypes.CLOSE_PARENTHESIS))
                    prBalance ++;
                else if(pairVector.elementAt(tokenPalace).getValue0().equals(TokenTypes.OPEN_PARENTHESIS))
                    prBalance --;
                tokenPalace--;
            }
        }
        return Math.max(0, tokenPalace);
    }

    private void popLastPairVectorElement()
    {
        pairVector.removeElementAt(pairVector.size() - 1);
    }

    private void methodCallHandler(boolean pointer)
    {
        popLastPairVectorElement();
        String methodName = pairVector.lastElement().getValue1();
        popLastPairVectorElement();
        popLastPairVectorElement();
        int methodCallPlace = findMethodCallerBlock();
        pairVector.add(methodCallPlace, new Pair<>(TokenTypes.OPEN_PARENTHESIS, openParenthesis));
        pairVector.add(methodCallPlace,
                new Pair<>(TokenTypes.OPEN_PARENTHESIS, openParenthesis + (pointer? "":and)));
        pairVector.add(methodCallPlace, new Pair<>(TokenTypes.ID, methodName));
        pairVector.add(new Pair<>(TokenTypes.CLOSE_PARENTHESIS, closeParenthesis));
        pairVector.add(new Pair<>(TokenTypes.ATTRIBUTE, comma + whiteSpace));
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
    }

    private void printfOpeningCPPFunctions()
    {
        write(newLine);
        write(thisManipulated + arrow + thisManipulated + whiteSpace + equalSign + whiteSpace + thisManipulated);
        write(semiColon + newLine);
//        for (String method: diagramInfo.getMethods(locationClass))
//        {
//            write(sharp + defineKeyword + whiteSpace +
//                    method + openParenthesis + ellipsis + closeParenthesis + whiteSpace +
//                    method + openParenthesis + thisManipulated + whiteSpace + comma + vaArgsToken + closeParenthesis
//                    + newLine);
////            write(sharp + defineKeyword + whiteSpace +
////                    method + openParenthesis + closeParenthesis + whiteSpace +
////                    method + openParenthesis + thisManipulated + closeParenthesis
////                    + newLine);
//        }
//        for (String attribute: diagramInfo.getAttributes(locationClass))
//            write(sharp + defineKeyword + whiteSpace +
//                    attribute + whiteSpace + thisManipulated + arrow + attribute + newLine);
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
        popLastPairVectorElement();
        pairVector.add(new Pair<>(TokenTypes.DESTRUCT, voidKeyword + whiteSpace + destructorKeyword));
        locationState = LocationState.DESTRUCTOR;
    }

    private void internalMethodShowedUp()
    {
        locationClass = lastClassCalled;
        popLastPairVectorElement();
        if(numberOfIDCalled > 0 || numberOfTypeSpecifierCalled - numberOfClassCalled > 0 || numberOfClassCalled > 1
            || numberOfClassKeywordCalled > 0 || numberOfStructureKeywordCalled > 0)
            locationState = LocationState.METHOD;
        else
        {
            pairVector.add(new Pair<>(TokenTypes.CONSTRUCTOR, voidKeyword + whiteSpace + constructorKeyword));
            locationState = LocationState.CONSTRUCTOR;
        }
    }

    private void classTypeCalled()
    {
        if (lineState == LineState.NEW_LINE && !locationState.equals(LocationState.OUTSIDE))
            lineState = LineState.POSSIBLY_INITIALIZER;
        if (numberOfClassCalled == 0)
            firstClassCalled = lastClassCalled;
        numberOfClassCalled ++;
        numberOfTypeSpecifierCalled ++;
    }

    private void classKeywordCalled()
    {
        numberOfStructureKeywordCalled++;
        numberOfClassKeywordCalled++;
    }

    private void changeToDefinitelyNotAInitializer()
    {
        if (!lineState.equals(LineState.RETURN_LINE))
            lineState = LineState.DEFINITELY_NOT_A_INITIALIZER;
    }

    private void generatePhase2(Vector<Pair<TokenTypes, String>> tokens)
    {
        this.depthOfParenthesis = 0;
        this.depthOfCurlyBracket = 0;
        this.locationState = LocationState.OUTSIDE;
        resetLineState();
        this.pairVector = new Vector<>();
        this.variableStack = new Vector<>();

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
                case CLOSE_SQUARE_BRACKET:
                case NEW:
                case DOT:
                case ARROW:
                case REFERENCE:
                case STAR:
                    pairVector.add(token);
                    break;
                case EMPTY_STRING:
                case COMMENT:
                    if(pairVector.size() == 0 || !pairVector.lastElement().getValue0().equals(TokenTypes.NEW))
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
                case ELLIPSIS:
                case TILDA:
                    changeToDefinitelyNotAInitializer();
                    pairVector.add(new Pair<>(TokenTypes.SEPARATOR, token.getValue1()));
                    break;
                case DELETE:
                    changeToDefinitelyNotAInitializer();
                    pairVector.add(new Pair<>(TokenTypes.SEPARATOR, deleteManipulate));
                    break;
                case OPERATOR_OR_ASSIGN:
                case STRING:
                case NUMBER:
                case COMMA:
                case MALLOC:
                case SIZEOF:
                    pairVector.add(new Pair<>(TokenTypes.SEPARATOR, token.getValue1()));
                    break;
                case DOUBLE_COLON:
                    changeToDefinitelyNotAInitializer();
                    internalMethodShowedUp();
                    break;
                case DESTRUCT:
                    changeToDefinitelyNotAInitializer();
                    destructorSignShowedUp();
                    break;
                case STRUCT:
                case UNION:
                case TYPES:
                case ENUM:
                    changeToDefinitelyNotAInitializer();
                    pairVector.add(new Pair<>(TokenTypes.TYPE_SPECIFIER, token.getValue1()));
                    typeSpecifierAdded();
                    break;
                case CLASS:
                    pairVector.add(new Pair<>(TokenTypes.CLASS, CLanguageTokens.unionKeyword));
                    classKeywordCalled();
                    break;
                case UNKNOWN:
                case MACRO:
                case COLON:
                case RESERVED_KEYWORD:
                    changeToDefinitelyNotAInitializer();
                    pairVector.add(token);
                    flush();
                    break;
                case SEMI_COLON:
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
