package com.wqa.compiler.lexical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Identifier{
    Map<String, String> ma;
    Map<String, String> invma;
    final int keyword_num=12;
    final String keyword[]={"包括","输入输出流","主函数","循环","如果","其它","返回","整数","长整数","单浮点","双浮点","无类型"};
    public HashSet<Character> unary_op;
    public HashSet<String> binary_op;
    public HashSet<Character> delimiter;
    public Identifier()
    {
        unary_op=new HashSet<>();
        binary_op=new HashSet<>();
        delimiter=new HashSet<>();
        unary_op.add('+');
        unary_op.add('-');
        unary_op.add('*');
        unary_op.add('/');
        unary_op.add('=');
        unary_op.add('>');
        unary_op.add('<');
        binary_op.add("+=");
        binary_op.add("-=");
        binary_op.add("*=");
        binary_op.add("/=");
        binary_op.add("==");
        binary_op.add(">=");
        binary_op.add("<=");
        delimiter.add(',');
        delimiter.add('(');
        delimiter.add(')');
        delimiter.add('{');
        delimiter.add('}');
        delimiter.add(';');
        ma = new HashMap<String, String>();
        invma=new HashMap<String,String>();
        for(int i=0;i<keyword_num;i++)
        {
            ma.put(keyword[i],""+(char)(i+'a'));
            invma.put(""+(char)(i+'a'),keyword[i]);
        }
        invma.put("1","标识符");
        invma.put("2","常量");
        invma.put("3","双目运算符");
        invma.put("4","单目运算符");
    }
    boolean IsKeyword(String a)
    {
        for(int j=0;j<keyword_num;j++)
        {
            if(keyword[j].equals(a)) {
                return true;
            }
        }
        return false;
    }
    public String f(String str)        //映射到一个字符来表示
    {
        return ma.get(str);
    }
    public  String inf(String str)
    {
        if(invma.containsKey(str))
        {
            return invma.get(str);
        }
        return str;
    }
    public static void main(String[] args)
    {
        Identifier identifier=new Identifier();
        System.out.println(identifier.inf("n"));
    }
}
