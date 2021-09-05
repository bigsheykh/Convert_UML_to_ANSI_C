package com.project.phase2CodeGeneration;

import com.project.lexicalAnalyzer.CLanguageTokens;
import com.project.lexicalAnalyzer.LexicalAnalyzer;
import com.project.lexicalAnalyzer.TokenTypes;
import org.javatuples.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

public class Phase2CodeFileManipulator {
    enum FileType {
        C,H,CPP
    }

    FileType fileType;
    DiagramInfo diagramInfo;
    Vector<Pair<TokenTypes,String>> pairVector;

    public Phase2CodeFileManipulator(FileType type, Path path, DiagramInfo diagramInfo) {
        this.fileType = type;
        this.diagramInfo = diagramInfo;
        this.pairVector = new Vector<>();
        Vector<Pair<TokenTypes, String>>  tokens = LexicalAnalyzer.getTokensOfPhase2Files(path.normalize().toString());
        Path generatedPath = Path.of("generate/", String.valueOf(path));
        try {
            FileWriter generateWriter = new FileWriter(String.valueOf(generatedPath));
            generatePhase2(tokens, generateWriter);
            generateWriter.flush();
            generateWriter.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void inLineFlush()
    {

    }

    private void outLineFlush()
    {

    }

    private void decreaseParenthesis()
    {

    }

    private void increaseParenthesis()
    {

    }
    private void decreaseCurlyBracket()
    {
    }

    private void increaseCurlyBracket()
    {

    }

    private void decreaseSquareBracket()
    {

    }

    private void increaseSquareBracket()
    {

    }

    private void addID()
    {

    }

    private void thisCalled()
    {

    }

    private void typeDetailAdded()
    {

    }

    private void typeSpecifierAdded()
    {

    }

    private void destructorSignShowedUp()
    {

    }

    private void internalMethodShowedUp()
    {

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

    }

    private void classKeywordCalled()
    {

    }

    private void sizeofFunction()
    {
        
    }
    
    private void newKeywordCalled()
    {
        
    }
    
    private void deleteKeywordCalled()
    {
        
    }
    
    private void generatePhase2(Vector<Pair<TokenTypes, String>> tokens, FileWriter writer)
    {
        try {
            for(Pair<TokenTypes, String> token:tokens)
            switch (token.getValue0())
            {
                case OPEN_PARENTHESIS:
                    increaseParenthesis();
                    break;
                case CLOSE_PARENTHESIS:
                    decreaseParenthesis();
                    break;
                case OPEN_CURLY_BRACKET:
                    increaseCurlyBracket();
                    break;
                case CLOSE_CURLY_BRACKET:
                    decreaseCurlyBracket();
                    break;
                case OPEN_SQUARE_BRACKET:
                    increaseSquareBracket();
                    break;
                case CLOSE_SQUARE_BRACKET:
                    decreaseSquareBracket();
                    break;
                case EMPTY_STRING:
                case COMMENT:
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
                        classTypeCalled();
                    }
                    else
                    {
                        pairVector.add(new Pair<>(TokenTypes.ID, token.getValue1()));
                        addID();
                    }
                break;
                case THIS:
                    if(!fileType.equals(FileType.C))
                    {
                        pairVector.add(token);
                        thisCalled();
                    }
                    else
                    {
                        pairVector.add(new Pair<>(TokenTypes.ID,token.getValue1()));
                        addID();
                    }
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
                case RESERVED_KEYWORD:
                case STRING:
                case NUMBER:
                case ELLIPSIS:
                case TILDA:
                case COMMA:
                case MALLOC: //TODO check again
                    pairVector.add(new Pair<>(TokenTypes.SEPARATOR, token.getValue1()));
                    inLineFlush();
                    break;
                case DOUBLE_COLON:
                    pairVector.add(token);
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
                    pairVector.add(token);
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
                case SIZEOF:
                    pairVector.add(token);
                    sizeofFunction();
                    break;
                case NEW:
                    pairVector.add(token);
                    newKeywordCalled();
                    break;
                case DELETE:
                    pairVector.add(token);
                    deleteKeywordCalled();
                    break;
                case UNKNOWN:
                case MACRO:
                case SEMI_COLON:
                case COLON:
                    outLineFlush();
                    writer.write(token.getValue1());
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
