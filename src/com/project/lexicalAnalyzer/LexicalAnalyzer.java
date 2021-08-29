package com.project.lexicalAnalyzer;

import com.project.CommandExecutor;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    public static boolean isNameOkayInC(String s) {
        if (s == null)
            return false;
        return Pattern.matches("[_a-zA-Z][_a-zA-Z0-9]*", s) && !Arrays.asList(CLanguageTokens.reservedKeyword).contains(s)
                && !Arrays.asList(CLanguageTokens.reservedTypes).contains(s) && !Arrays.asList(CLanguageTokens.reservedTypeDescribers).contains(s);
    }

    public static String deleteTypeQualifier(String s) {
        if (s.startsWith(CLanguageTokens.constKeyword))
            return s.substring(CLanguageTokens.constKeyword.length() + 1);
        if (s.startsWith(CLanguageTokens.volatileKeyword))
            return s.substring(CLanguageTokens.volatileKeyword.length() + 1);
        return s;
    }

    private static String deleteClassSpecifier(String s) {
        if (s.startsWith(CLanguageTokens.extern))
            return deleteClassSpecifier(s.substring(CLanguageTokens.extern.length() + 1));
        if (s.startsWith(CLanguageTokens.typedef))
            return deleteClassSpecifier(s.substring(CLanguageTokens.typedef.length() + 1));
        if (s.startsWith(CLanguageTokens.staticKeyword))
            return deleteClassSpecifier(s.substring(CLanguageTokens.staticKeyword.length() + 1));
        if (s.startsWith(CLanguageTokens.auto))
            return deleteClassSpecifier(s.substring(CLanguageTokens.auto.length() + 1));
        if (s.startsWith(CLanguageTokens.register))
            return deleteClassSpecifier(s.substring(CLanguageTokens.register.length() + 1));
        return s;
    }

    private static String unStruct(String s) {
        if (s.startsWith(CLanguageTokens.struct))
            return s.substring(CLanguageTokens.struct.length() + 1);
        if (s.startsWith(CLanguageTokens.union))
            return s.substring(CLanguageTokens.union.length() + 1);
        if (s.startsWith(CLanguageTokens.classKeyword))
            return s.substring(CLanguageTokens.classKeyword.length() + 1);
        if (s.startsWith(CLanguageTokens.enumKeyword))
            return s.substring(CLanguageTokens.enumKeyword.length() + 1);//TODO edit equals mistake

        return s;
    }

    public static boolean isTypeOkayInC(String s) {
        if (s == null)
            return false;
        return isNameOkayInC(unStruct(deleteClassSpecifier(s))) ||
                Arrays.asList(CLanguageTokens.reservedTypes).contains(deleteClassSpecifier(s));
    }

    public static Vector<Pair<TokenTypes, String>> getTokensOfPhase2Files(String fileName) {
        Vector<Pair<TokenTypes, String>> result = new Vector<>();
        String lexOutput = fileName + ".result";
        Scanner lexScanner;
        CommandExecutor.executeCommand(CommandExecutor.lexerCommand + " " + fileName + " " + lexOutput);
        FileWriter unknownWriter;
        try {
            lexScanner = new Scanner(new File("result/" + lexOutput));
            unknownWriter = new FileWriter("unknown.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return result;
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
                return result;
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
                default:
                    tokenType = TokenTypes.UNKNOWN;
                    try {
                        unknownWriter.write(type + ":" + "\n");
                        unknownWriter.write(value);
                        unknownWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            result.add(new Pair<>(tokenType, value));
        }
        try {
            unknownWriter.flush();
            unknownWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //TODO deleteTypeQualifier
}
