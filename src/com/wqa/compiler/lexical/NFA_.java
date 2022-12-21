package com.wqa.compiler.lexical;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
public class NFA_ {
    String lexicalGrammar;
    public static int startState;
    public HashSet<Character> finall = new HashSet<>();//存储终结符
    public HashSet<Integer> state = new HashSet<>();//存储状态
    final int N=300;
    public int h[],w[],ne[],e[],idx;//链式前向星数组存图
    public NFA_(String lexicalGrammar) throws IOException {
        this.lexicalGrammar=lexicalGrammar;
        h=new int[N];
        w=new int[N];
        ne=new int[N];
        e=new int[N];
        for(int i=0;i<N;i++)
        {
            h[i]=-1;
        }
        createNFA();
        print_NFA();
    }
    void add(int a,int b,int c)
    {
        e[idx]=b;
        w[idx]=c;
        ne[idx]=h[a];
        h[a]=idx++;
    }
    void createNFA() throws IOException {
        String code ;
        BufferedReader in = new BufferedReader( new FileReader(lexicalGrammar));
        int i=0;
        while ((code = in.readLine()) != null)//读取每一行文法
        {
            int n=code.length();
            if(n==0)continue;
            int a,b;
            char c=' ';
            a=(code.charAt(3)==']'?code.charAt(2)-'0':(code.charAt(2)-'0')*10+code.charAt(3)-'0');//读出是第几个状态

            for(int j=0;j<n;j++)
            {
                if(code.charAt(j)=='>'){
                    c=code.charAt(j+1);
                    break;
                }
            }
            finall.add(c);//找出终结符
            state.add(a);//状态
            if(i==0){
                startState=a;//确定初态
                i++;
            }
            if(n>8)
            {
                b=(code.charAt(n-3)=='S'?code.charAt(n-2)-'0':(code.charAt(n-3)-'0')*10+(code.charAt(n-2)-'0'));
                add(a,b,c);//加边函数，构建出NFA
            }else
            {
                b=100;//将100作为终态
                add(a,b,c);
            }
            state.add(b);
        }
    }
    void print_NFA()
    {
        for(int a:state)
        {
            for(int i=h[a];i!=-1;i=ne[i])
            {
                int j=e[i];
                System.out.println("状态[S"+a+"] 通过"+(char)w[i]+"  转到状态[S"+j+"]");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new NFA_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\wenfa.txt");

    }
}
