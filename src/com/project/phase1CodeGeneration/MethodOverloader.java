package com.project.phase1CodeGeneration;

import java.util.*;

public class MethodOverloader {
    private static class RealFunction
    {
        public int numberOfArgument;
        public String mangledName;
        public Vector<CompleteValueType> params;

        public RealFunction(int numberOfArgument, String mangledName, Vector<CompleteValueType> params) {
            this.numberOfArgument = numberOfArgument;
            this.mangledName = mangledName;
            this.params = params;
        }
    }
    private HashMap<String, Vector<RealFunction>> overloadTable;

    public static String randomGenerator()
    {
        String allLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int numberOfChars = allLetters.length();
        Random r = new Random();
        StringBuilder value = new StringBuilder();
        value.append('_');
        while (value.length() < 10)
            value.append(allLetters.charAt(r.nextInt(numberOfChars)));
        return value.toString();
    }


}
