package com.project.phase2CodeGeneration;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private static final String[] reservedKeyword = new String[]{
            "_Packed", "break", "case", "continue", "default",
            "do", "else", "enum", "for", "goto", "if", "return",
            "signed", "sizeof", "switch", "unsigned", "while"
    };

    private static final String[] notUsefulCharacters = new String[]{

    };

    private static final String openParenthesis = "(";
    private static final String closeParenthesis = ")";
    private static final String openCurlyBracket = "{";
    private static final String CloseCurlyBracket = "}";
    private static final String openSquareBracket = "[";
    private static final String CloseSquareBracket = "]";

    private static final String ellipsis = "...";
    private static final String star = "*";
    private static final String arrow = "->";
    private static final String Dot = ".";
    private static final String comma = ",";
    private static final String semiColon = ";";
    private static final String colon = ":";
    private static final String tilde = "~";
    private static final String doubleColon = "::";

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

    //TODO type
    //TODO deleteTypeQualifier
    //TODO Integrate with clex.py
}
