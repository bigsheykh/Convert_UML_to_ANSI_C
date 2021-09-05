package com.project.lexicalAnalyzer;

import com.project.CommandExecutor;
import org.javatuples.Pair;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

public interface LexicalAnalyzer {

    private static boolean ifPlusWhiteSpaceIsSubstring(String s, String keyword)
    {
       return s.startsWith(keyword + whiteSpace) ;
    }

    static String getWithoutArrayBracket(String s)
    {
        if(!s.endsWith(closeSquareBracket))
                return s;
        for(int i = s.length() - 2; i > 0; i--)
            if(s.toCharArray()[i] == openSquareBracket.toCharArray()[0])
            {
                if(Pattern.matches("[1-9][0-9]*", s.substring(i + 1, s.length() - 1)))
                    return getWithoutArrayBracket(s.substring(0, i));
                return null;
            }
        return null;
    }

    static boolean isStructNameOkayInC(String name)
    {
        return isNameOkayInC(name) && !name.contains("[");
    }
    static boolean isNameOkayInC(String name) {
        if (name == null)
            return false;
        String s = getWithoutArrayBracket(name);
        if(s == null)
            return false;
        return Pattern.matches("[_a-zA-Z][_a-zA-Z0-9]*", s) && !Arrays.asList(reservedKeyword).contains(s)
                && !Arrays.asList(reservedTypes).contains(s) && !Arrays.asList(reservedTypeDescribers).contains(s);
    }

    static String deleteTypeQualifier(String s) {
        if (ifPlusWhiteSpaceIsSubstring(s, constKeyword))
            return s.substring(constKeyword.length() + 1);
        if (ifPlusWhiteSpaceIsSubstring(s, volatileKeyword))
            return s.substring(volatileKeyword.length() + 1);
        return s;
    }

    private static String deleteClassSpecifier(String s) {
        s = deleteTypeQualifier(s);
        if (ifPlusWhiteSpaceIsSubstring(s, extern))
            return deleteClassSpecifier(s.substring(extern.length() + 1));
        if (ifPlusWhiteSpaceIsSubstring(s, typedef))
            return deleteClassSpecifier(s.substring(typedef.length() + 1));
        if (ifPlusWhiteSpaceIsSubstring(s, staticKeyword))
            return deleteClassSpecifier(s.substring(staticKeyword.length() + 1));
        if (ifPlusWhiteSpaceIsSubstring(s, auto))
            return deleteClassSpecifier(s.substring(auto.length() + 1));
        if (ifPlusWhiteSpaceIsSubstring(s, register))
            return deleteClassSpecifier(s.substring(register.length() + 1));
        return s;
    }

    private static String unStruct(String s) {
        s = deleteTypeQualifier(s);
        if (ifPlusWhiteSpaceIsSubstring(s, structKeyword))
            return s.substring(structKeyword.length() + 1);
        if (ifPlusWhiteSpaceIsSubstring(s, unionKeyword))
            return s.substring(unionKeyword.length() + 1);
        if (ifPlusWhiteSpaceIsSubstring(s, classKeyword))
            return s.substring(classKeyword.length() + 1);
        if (ifPlusWhiteSpaceIsSubstring(s, enumKeyword))
            return s.substring(enumKeyword.length() + 1);

        return s;
    }

    static boolean isTypeOkayInC(String s) {
        if (s == null || s.contains("["))
            return false;
        return isNameOkayInC(deleteTypeQualifier(unStruct(deleteClassSpecifier(s)))) ||
                Arrays.asList(reservedTypes).contains(deleteClassSpecifier(s));
    }

    static Vector<Pair<TokenTypes, String>> getTokensOfPhase2Files(String fileName) {
        Vector<Pair<TokenTypes, String>> result = new Vector<>();
        String lexOutput = fileName + ".result";
        Scanner lexScanner;
        CommandExecutor.executeCommand(CommandExecutor.lexerCommand + " " + fileName + " " + lexOutput);
        FileWriter unknownWriter;
        try {
            File resultFile = new File("result/" + lexOutput);
            int counter = 10000;
            while (!resultFile.exists() && counter > 0) counter--;
            lexScanner = new Scanner(resultFile);
            unknownWriter = new FileWriter("unknown.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int counter = lexScanner.nextInt();
        for (int i = 0; i < counter; i++) {
            String type, value;
            try {
                String resultBasePath = "result/" + lexOutput + i;
                Scanner typeScanner = new Scanner(new File(resultBasePath + ".type"));
                type = typeScanner.next();
                Path valueNamePath = Path.of(resultBasePath + ".value");
                value = Files.readString(valueNamePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            TokenTypes tokenType;
            switch (type) {
                case "AUTO":
                    tokenType = TokenTypes.AUTO;
                    break;
                case "CHAR":
                case "DOUBLE":
                case "FLOAT":
                case "INT":
                case "LONG":
                case "SHORT":
                case "VOID":
                    tokenType = TokenTypes.TYPES;
                    break;
                case "CONST":
                    tokenType = TokenTypes.CONST;
                    break;
                case "ENUM":
                    tokenType = TokenTypes.ENUM;
                    break;
                case "EXTERN":
                    tokenType = TokenTypes.EXTERN;
                    break;
                case "REGISTER":
                    tokenType = TokenTypes.REGISTER;
                    break;
                case "SIGNED":
                case "UNSIGNED":
                    tokenType = TokenTypes.SIGN;
                    break;
                case "SIZEOF":
                    tokenType = TokenTypes.SIZEOF;
                    break;
                case "STATIC":
                    tokenType = TokenTypes.STATIC;
                    break;
                case "STRUCT":
                    tokenType = TokenTypes.STRUCT;
                    break;
                case "TYPEDEF":
                    tokenType = TokenTypes.TYPEDEF;
                    break;
                case "UNION":
                    tokenType = TokenTypes.UNION;
                    break;
                case "VOLATILE":
                    tokenType = TokenTypes.VOLATILE;
                    break;
                case "CLASS":
                    tokenType = TokenTypes.CLASS;
                    break;
                case "THIS":
                    tokenType = TokenTypes.THIS;
                    break;
                case "ID":
                    tokenType = TokenTypes.ID;
                    break;
                case "ICONST":
                case "FCONST":
                    tokenType = TokenTypes.NUMBER;
                    break;
                case "SCONST":
                case "CCONST":
                    tokenType = TokenTypes.STRING;
                    break;
                case "TIMES":
                    tokenType = TokenTypes.STAR;
                    break;
                case "AND":
                    tokenType = TokenTypes.REFERENCE;
                    break;
                case "ARROW":
                    tokenType = TokenTypes.ARROW;
                    break;
                case "LPAREN":
                    tokenType = TokenTypes.OPEN_PARENTHESIS;
                    break;
                case "RPAREN":
                    tokenType = TokenTypes.CLOSE_PARENTHESIS;
                    break;
                case "LBRACKET":
                    tokenType = TokenTypes.OPEN_SQUARE_BRACKET;
                    break;
                case "RBRACKET":
                    tokenType = TokenTypes.CLOSE_SQUARE_BRACKET;
                    break;
                case "LBRACE":
                    tokenType = TokenTypes.OPEN_CURLY_BRACKET;
                    break;
                case "RBRACE":
                    tokenType = TokenTypes.CLOSE_CURLY_BRACKET;
                    break;
                case "COMMA":
                    tokenType = TokenTypes.COMMA;
                    break;
                case "PERIOD":
                    tokenType = TokenTypes.DOT;
                    break;
                case "SEMI":
                    tokenType = TokenTypes.SEMI_COLON;
                    break;
                case "COLON":
                    tokenType = TokenTypes.COLON;
                    break;
                case "DOUBLECOLON":
                    tokenType = TokenTypes.DOUBLE_COLON;
                    break;
                case "TILDA":
                    tokenType = TokenTypes.TILDA;
                    break;
                case "DESTRUCT":
                    tokenType = TokenTypes.DESTRUCT;
                    break;
                case "preprocessor":
                    tokenType = TokenTypes.MACRO;
                    break;
                case "LINECOMMENT":
                case "comment":
                    tokenType = TokenTypes.COMMENT;
                    break;
                case "TAB":
                case "WHITESPACE":
                case "NEWLINE":
                    tokenType = TokenTypes.EMPTY_STRING;
                    break;
                case "ELLIPSIS":
                    tokenType = TokenTypes.ELLIPSIS;
                    break;
                case "MALLOC":
                    tokenType = TokenTypes.MALLOC;
                    break;
                case "NEW":
                    tokenType = TokenTypes.NEW;
                    break;
                case "PLUS":
                case "MINUS":
                case "DIVIDE":
                case "MOD":
                case "OR":
                case "NOT":
                case "XOR":
                case "LSHIFT":
                case "RSHIFT":
                case "LOR":
                case "LAND":
                case "LNOT":
                case "LT":
                case "LE":
                case "GT":
                case "GE":
                case "EQ":
                case "NE":
                case "EQUALS":
                case "TIMESEQUAL":
                case "DIVEQUAL":
                case "MODEQUAL":
                case "PLUSEQUAL":
                case "MINUSEQUAL":
                case "LSHIFTEQUAL":
                case "RSHIFTEQUAL":
                case "ANDEQUAL":
                case "XOREQUAL":
                case "OREQUAL":
                case "PLUSPLUS":
                case "MINUSMINUS":
                case "CONDOP":
                    tokenType = TokenTypes.OPERATOR_OR_ASSIGN;
                    break;
                case "BREAK":
                case "CASE":
                case "CONTINUE":
                case "DEFAULT":
                case "DO":
                case "ELSE":
                case "FOR":
                case "GOTO":
                case "IF":
                case "RETURN":
                case "SWITCH":
                case "WHILE":
                    tokenType = TokenTypes.RESERVED_KEYWORD;
                    break;
                case "DELETE":
                    tokenType = TokenTypes.DELETE;
                    break;
                case "PARENT":
                    tokenType = TokenTypes.PARENT;
                    break;
                case "CONSTRUCTOR":
                    tokenType = TokenTypes.CONSTRUCTOR;
                    break;
                case "DESTRUCTOR":
                    tokenType = TokenTypes.DESTRUCTOR;
                    break;
                case "METHOD":
                    tokenType = TokenTypes.METHOD;
                    break;
                case "ATTRIBUTE":
                    tokenType = TokenTypes.ATTRIBUTE;
                    break;
                default:
                    tokenType = TokenTypes.UNKNOWN;
                    try {
                        unknownWriter.write(type + ":" + "\n");
                        unknownWriter.write(value);
                        unknownWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
            }
            result.add(new Pair<>(tokenType, value));
        }
        try {
            unknownWriter.flush();
            unknownWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    //TODO deleteTypeQualifier
}
