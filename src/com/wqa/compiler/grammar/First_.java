package com.wqa.compiler.grammar;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class First_ {
    Grammar_ grammar_;
    Map<Character, HashSet<Character>> first_set;
    public static String first_str="";
    public First_(String grammarFile) throws IOException {
        first_set=new HashMap<>();
        grammar_=new Grammar_(grammarFile);
        for(char a:grammar_.state)
        {
            first_set.put(a,new HashSet<>());
        }
        get_firstset();
        print_firstset();
    }
    void get_firstset()
    {
        boolean haschange=true;
        while(haschange)
        {
            haschange=false;
            for(int i=0;i<=grammar_.count;i++)
            {
                char S=grammar_.grammar[i].charAt(0);
                int j=0;
                for(j=3;j<grammar_.grammar[i].length();j++)
                {
                    char a=grammar_.grammar[i].charAt(j);
                    if(Character.isUpperCase(a))
                    {
                        boolean flag=false;
                        for(char b:first_set.get(a))
                        {
                            if(b=='@')
                            {
                                flag=true;
                                continue;
                            }
                            if(first_set.get(S).contains(b)==false)
                            {
                                first_set.get(S).add(b);
                                haschange=true;
                            }
                        }
                        if(flag==false)break;
                    }else
                    {
                        if(first_set.get(S).contains(a)==false)
                        {
                            first_set.get(S).add(a);
                            haschange=true;
                        }
                        break;
                    }
                }//只有该产生式右边所有符号都能推出空，才能把空加入到first集合中
                if(j==grammar_.grammar[i].length())first_set.get(S).add('@');
            }
        }
    }
    void print_firstset()
    {
        for(char a:grammar_.state)
        {
            first_str+="First("+a+")={";
           // System.out.print("First"+a+"={");
            int i=0;
            for(char b:first_set.get(a))
            {
                first_str+=b;
                i++;
                if(i<first_set.get(a).size())first_str+=",";
                //System.out.print(b+",");
            }
            first_str+="}\n";
            //System.out.println("}");
        }
    }
    public static void main(String[] args) throws IOException {
        First_ first_ = new First_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\grammar\\yufa.txt");
        System.out.println(First_.first_str);

    }
}
