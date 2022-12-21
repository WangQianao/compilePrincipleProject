package com.wqa.compiler.grammar;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

public class Grammar_ {

    String grammarFile;
    String[] grammar;
    public static String grammar_str="";
    public int count=0;
    public HashSet<Character> finall = new HashSet<>();//存储终结符
    public HashSet<Character> state = new HashSet<>();//存储非终结符
    final int N=300;
    public Grammar_(String grammarFile) throws IOException {
        this.grammarFile=grammarFile;
        grammar=new String[N];
        finall.add('#');
        read_grammar();
        print_grammar();
    }
    void read_grammar() throws IOException {
        String code;
        BufferedReader in = new BufferedReader( new java.io.FileReader(grammarFile));
        while ((code = in.readLine()) != null) {
            if(code.length()==0)continue;
            if(count==0)
            {
                grammar[count]="S->"+code.charAt(0);
            }
            grammar[++count]=code;
        }
        for(int i=0;i<=count;i++)
        {
            for(int j=0;j<grammar[i].length();j++)
            {
                if(j==1||j==2)continue;
                char a=grammar[i].charAt(j);
                if(Character.isUpperCase(a))state.add(a);
                else finall.add(a);
            }
        }
    }
    void print_grammar()
    {
        for(int i=0;i<= count;i++)
        {
            grammar_str+=grammar[i]+"\n";
            //System.out.println(grammar[i]);
        }
    }
    public static void main(String[] args) throws IOException {
        Grammar_ grammar_ = new Grammar_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\grammar\\yufa.txt");
        System.out.println(grammar_str);

    }
}
