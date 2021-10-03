package com.project.phase1CodeGeneration;

import com.project.classBaseUML.ValueType;

import java.util.*;
import java.util.stream.Collectors;

import static com.project.lexicalAnalyzer.CLanguageTokens.*;

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

    private static MethodOverloader instance = null;
    private HashMap<String, Vector<RealFunction>> overloadTable;
    private int maxNumberOfParams;
    private static String choose = "CHOOSE";

    private MethodOverloader()
    {
        overloadTable = new HashMap<>();
    }

    public static MethodOverloader getInstance() {
        if(instance == null)
        {
            instance = new MethodOverloader();
            instance.maxNumberOfParams = 0;
        }
        return instance;
    }

    public static HashMap<String, Vector<RealFunction>> getOverloadTable() {
        return getInstance().overloadTable;
    }

    public static void addToTable(String realName, String mangledName, String className,
                                  Vector<CompleteAttribute> params)
    {
        Vector<CompleteValueType> valueTypes = new Vector<>();
        if(className != null)
            valueTypes.add(new CompleteValueType(
                    new ValueType(unionKeyword + whiteSpace + className, 1)));
        for(CompleteAttribute attribute:params)
            valueTypes.add(attribute.getValueType());
        if(!getOverloadTable().containsKey(realName))
            getOverloadTable().put(realName, new Vector<>());
        getOverloadTable().get(realName).add(new RealFunction(params.size(), mangledName, valueTypes));
        instance.maxNumberOfParams = Math.max(params.size(), instance.maxNumberOfParams);
    }
    public static String generateSelectArg(int i)
    {
        return "SELECT_" + i;
    }

    public static String generate1LineChoose(RealFunction function)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(choose).append(openParenthesis);
        for(int i = 0;i < function.params.size(); i++)
        {
            builder.append(iftypeKeyword + openParenthesis).append(generateSelectArg(i + 1))
                    .append(openParenthesis).append(vaArgsToken).append(closeParenthesis).append(comma + whiteSpace)
                    .append(function.params.get(i).getShowName()).append(closeParenthesis);
            if(i != function.params.size() - 1)
                builder.append(whiteSpace).append(and.repeat(2)).append(whiteSpace);
        }
        builder.append(comma + whiteSpace).append(function.mangledName).append(comma + whiteSpace + backSlashNewLine);
        return builder.toString();
    }
    public static String generateBasedParamSize(String mangledName, List<RealFunction> realFunctions)
    {
        StringBuilder builder = new StringBuilder();
        if(realFunctions.size() == 0)
            return "";
        builder.append(sharp + defineKeyword + whiteSpace)
                .append(mangledName).append(openParenthesis).append(ellipsis).append(closeParenthesis);
        builder.append(whiteSpace + backSlashNewLine);
        for(int i = 1;i < realFunctions.size(); i++)
            builder.append(generate1LineChoose(realFunctions.get(i)));
        builder.append(realFunctions.get(0).mangledName).append(closeParenthesis.repeat(realFunctions.size() - 1))
                .append(whiteSpace + backSlashNewLine);
        builder.append(openParenthesis + vaArgsToken + closeParenthesis + newLine);
        return builder.toString();
    }

    public static String generate1NameOverload(String realName, Vector<RealFunction> realFunctions)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i < instance.maxNumberOfParams + 4; i++) {
            int finalI = i;
            builder.append(generateBasedParamSize(realName + i,
                    realFunctions.stream().filter(r-> r.params.size() == finalI).collect(Collectors.toList())));
        }
        builder.append(newLine);
        return builder.toString();
    }
    public static String generateOverloadMacros()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("#define CHOOSE __builtin_choose_expr\n");
        builder.append("#define IFTYPE(X, T) __builtin_types_compatible_p(typeof(X), T)\n");
        builder.append(newLine);


        for(int i = 1; i < instance.maxNumberOfParams + 4;i++)
        {
            builder.append(sharp + defineKeyword + whiteSpace).append(generateSelectArg(i)).append(openParenthesis);
            for(int j = 1; j <=i;j++)
                builder.append("X").append(j).append(", ");
            builder.append(ellipsis + closeParenthesis + whiteSpace + "X").append(i).append(newLine);
        }
        builder.append(newLine);

        for(String realName:getOverloadTable().keySet())
            builder.append(generate1NameOverload(realName, getOverloadTable().get(realName)));
        builder.append(newLine);

        builder.append(sharp + defineKeyword + whiteSpace + select_N + openParenthesis + "X, ");
        for(int i = 1; i < instance.maxNumberOfParams + 4;i++)
            builder.append(underscore).append(i).append(comma).append(whiteSpace);
        builder.append("N" + comma + whiteSpace + ellipsis + closeParenthesis + whiteSpace + "N" + newLine);
        builder.append(newLine);

        for(String methodName:getOverloadTable().keySet())
        {
            builder.append(sharp + defineKeyword + whiteSpace).append(methodName)
                    .append(openParenthesis + ellipsis + closeParenthesis)
                    .append(whiteSpace + select_N + openParenthesis + "X" + comma + whiteSpace)
                    .append(sharp + sharp + vaArgsToken);
            for(int i = instance.maxNumberOfParams + 3; i >= 0;i--)
                builder.append(comma).append(whiteSpace).append(methodName).append(i);
            builder.append(closeParenthesis);
            builder.append(openParenthesis + vaArgsToken + closeParenthesis + newLine);
        }
        return builder.toString();
    }


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
