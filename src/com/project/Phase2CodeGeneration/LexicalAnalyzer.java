package com.project.Phase2CodeGeneration;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    public static final String[] reservedKeyword = new String[]{"_Packed", "auto", "break",
            "case", "const", "continue", "default",
            "do", "else", "enum", "extern",
            "for", "goto", "if", "register",
            "return", "signed", "sizeof", "static",
            "struct", "switch", "typedef", "union",
            "unsigned", "volatile", "while"
    };
    public static final String[] reservedTypes = new String[]{
            "char", "signed char", "unsigned char",
            "short", "short int", "signed short", "signed short int", "unsigned short", "unsigned short int",
            "int", "signed", "signed int", "unsigned", "unsigned int",
            "long", "long int", "signed long", "signed long int", "unsigned long", "unsigned long int",
            "long long", "long long int", "signed long long", "signed long long int",
            "unsigned long long", "unsigned long long int",
            "float", "double", "long double"
    };

    public static boolean isNameOkayInC(String s) {
        if (s == null)
            return false;
        return Pattern.matches("[_a-zA-Z][_a-zA-Z0-9]*", s) && !Arrays.asList(reservedKeyword).contains(s)
                && !Arrays.asList(reservedTypes).contains(s);
    }

    public static String unRegister(String s) {
        String register = "register ";
        if (s.length() < register.length() || !s.startsWith(register))
            return s;
        return s.substring(register.length());
    }

    public static String unStruct(String s) {
        String struct = "struct ";
        if (s.length() < struct.length() || !s.startsWith(struct))
            return s;
        return s.substring(struct.length());
    }

    public static boolean isTypeOkayInC(String s) {
        if (s == null)
            return false;
        return isNameOkayInC(unRegister(unStruct(s))) || Arrays.asList(reservedTypes).contains(unRegister(s));
    }
}
