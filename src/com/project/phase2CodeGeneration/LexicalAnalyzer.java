package com.project.phase2CodeGeneration;

import com.project.CommandExecutor;
import org.javatuples.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private static final String[] reservedKeyword = new String[]{
            "_Packed", "break", "case", "continue", "default",
            "do", "else", "enum", "for", "goto", "if", "return",
            "signed", "sizeof", "switch", "unsigned", "while"
    };

    private static final String constKeyword = "const";
    private static final String volatileKeyword = "volatile";
    private static final String extern = "extern";
    private static final String struct = "struct";
    private static final String union = "union";
    private static final String classKeyword = "class";
    private static final String typedef = "typedef";
    private static final String staticKeyword = "static";
    private static final String auto = "auto";
    private static final String register = "register";
    private static final String enumKeyword = "enum";

    private static final String[] reservedTypeDescribers = new String[]{
            constKeyword, volatileKeyword, extern, struct, union,
            classKeyword, typedef, staticKeyword, auto, register
    };

    private static final String[] reservedTypes = new String[]{
            "char", "signed char", "unsigned char",
            "short", "short int", "signed short", "signed short int", "unsigned short", "unsigned short int",
            "int", "signed", "signed int", "unsigned", "unsigned int",
            "long", "long int", "signed long", "signed long int", "unsigned long", "unsigned long int",
            "long long", "long long int", "signed long long", "signed long long int",
            "unsigned long long", "unsigned long long int",
            "float", "double", "long double", "void"
    };

    public static boolean isNameOkayInC(String s) {
        if (s == null)
            return false;
        return Pattern.matches("[_a-zA-Z][_a-zA-Z0-9]*", s) && !Arrays.asList(reservedKeyword).contains(s)
                && !Arrays.asList(reservedTypes).contains(s) && !Arrays.asList(reservedTypeDescribers).contains(s);
    }

    public static String deleteTypeQualifier(String s)
    {
        if (s.startsWith(constKeyword))
            return s.substring(constKeyword.length() + 1);
        if (s.startsWith(volatileKeyword))
            return s.substring(volatileKeyword.length() + 1);
        return s;
    }
    private static String deleteClassSpecifier(String s)
    {
        if (s.startsWith(extern))
            return deleteClassSpecifier(s.substring(extern.length() + 1));
        if (s.startsWith(typedef))
            return deleteClassSpecifier(s.substring(typedef.length() + 1));
        if (s.startsWith(staticKeyword))
            return deleteClassSpecifier(s.substring(staticKeyword.length() + 1));
        if (s.startsWith(auto))
            return deleteClassSpecifier(s.substring(auto.length() + 1));
        if (s.startsWith(register))
            return deleteClassSpecifier(s.substring(register.length() + 1));
        return s;
    }

    private static String unStruct(String s) {
        if (s.startsWith(struct))
            return s.substring(struct.length() + 1);
        if (s.startsWith(union))
            return s.substring(union.length() + 1);
        if (s.startsWith(classKeyword))
            return s.substring(classKeyword.length() + 1);
        if (s.startsWith(enumKeyword))
            return s.substring(enumKeyword.length() + 1);

        return s;
    }

    public static boolean isTypeOkayInC(String s) {
        if (s == null)
            return false;
        return isNameOkayInC(unStruct(deleteClassSpecifier(s))) ||
                Arrays.asList(reservedTypes).contains(deleteClassSpecifier(s));
    }

    public static Vector<Pair<TokenTypes, String>> getTokensOfPhase2Files(String fileName)
    {
        Vector<Pair<TokenTypes, String>> result = new Vector<>();
        String lexOutput = fileName + ".result";
        Scanner lexScanner;
        CommandExecutor.executeCommand(CommandExecutor.lexerCommand + " " + fileName + " " + lexOutput);
        FileWriter myWriter;
        try {
            lexScanner = new Scanner(new File("result/" + lexOutput));
            myWriter = new FileWriter("filename.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }

        int counter = lexScanner.nextInt();
        for (int i = 0; i < counter; i++)
        {
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
                default:
                    tokenType = TokenTypes.UNKNOWN;
                    try {
                        myWriter.write(type + ":" + "\n");
                        myWriter.write(value);
                        myWriter.write("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            result.add(new Pair<>(tokenType, value));
        }
        try {
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //TODO type
    //TODO deleteTypeQualifier
    //TODO Integrate with clex.py
}
