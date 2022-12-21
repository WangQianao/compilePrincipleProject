package com.wqa.compiler.lexical;

import java.io.IOException;
import java.util.*;

public class DFA_ {
    Vector<HashSet<Integer>> subset = new Vector<>();
    NFA_ nfa;
    final int N=300;
    public int h[],ne[],w[],e[],idx;//链式前向星存图
    public DFA_(String lexicalGrammar) throws IOException {
        h=new int[N];
        ne=new int[N];
        w=new int[N];
        e=new int[N];
        for(int i=0;i<N;i++)h[i]=-1;
        nfa=new NFA_(lexicalGrammar);
        nfa2dfa();
        print_subset();
        print_dfa();
    }
    boolean is_final_state(int state)//判断这个子集是否包含终态
    {
        for(int x:subset.get(state))//遍历一下子集中的所有状态
        {
            if(x==100)return true;
        }
        return false;
    }
    void add(int a,int b,int c)//加边函数
    {
        e[idx]=b;
        w[idx]=c;
        ne[idx]=h[a];
        h[a]=idx++;
    }
    void getClosure(HashSet<Integer> state)//求闭包函数
    {
        boolean hasChange=true;
        boolean st[]=new boolean[300];//用来判断状态集中的这个状态是否已经被拿出来做过分析了，如果已经拿出来做过分析，就没必要继续再做了
        HashSet<Integer> temp=new HashSet<>();//保存所有扩展到的新状态
        //求状态集中所有状态经过任意条空边能到达的状态
        while(hasChange)//只要还有改动，就要继续做下去
        {
            hasChange=false;
            for(int x:state)//遍历原有的所有状态
            {
                if(st[x]==true)continue;
                st[x]=true;
                for (int i = nfa.h[x]; i != -1; i = nfa.ne[i])//对于这个状态，将它通过空边能到达的所有状态，并且没有加入到集合中的，加入
                {
                    int j=nfa.e[i];
                    if((char)nfa.w[i]!='@')continue;
                    if(state.contains(j)==false)
                    {
                        hasChange=true;
                        temp.add(j);
                    }
                }
            }
            for(int x:temp)state.add(x);//将所有新扩展到的状态加入到原状态集
        }
    }
    public HashSet<Integer> move(HashSet<Integer> state,char a)//求a弧转换
    {
        //状态集中所有状态经过一条a弧边能到达的所有状态
        HashSet<Integer> res=new HashSet<>();//这里不能将扩展到的状态加入原先的状态集，需要新开一个状态集
        for(int x:state)
        {
            for(int i=nfa.h[x];i!=-1;i=nfa.ne[i])
            {
                int j=nfa.e[i];
                if((char)nfa.w[i]!=a)continue;
                res.add(j);
            }
        }
        return res;
    }
    public boolean judge_new(HashSet<Integer> state)//判断这个状态集是否已经出现过，返回true表示是新状态，返回false表示是假状态
    {
        for(HashSet<Integer> x:subset)//遍历所有状态集，看看有没有与当前集合相同的
        {
            if(x.size()!=state.size())continue;//如果两个集合里的状态数目都不相同，那么这两个集合肯定不相同
            boolean flag=true;
            for(int y:state)
            {
                if(x.contains(y)==false)
                {
                    flag=false;
                    break;
                }
            }
            if(flag==true)return false;//表示已经找到了一个一模一样的
        }
        return true;
    }
    public int get_state(HashSet<Integer> state)//返回该子集在所有子集中的下标,没有则返回-1
    {
        for(int i=0;i< subset.size();i++)
        {
            if(state.size()!=subset.get(i).size())continue;
            boolean flag=true;
            for(int y:subset.get(i))
            {
                if(state.contains(y)==false)
                {
                    flag=false;
                    break;
                }
            }
            if(flag==true)return i;
        }
        return -1;
    }

    public void nfa2dfa()
    {
        //System.out.println(NFA_.startState);
        HashSet<Integer> begin_state=new HashSet<>();
        begin_state.add(NFA_.startState);
        getClosure(begin_state);
        Queue<HashSet<Integer>> q=new LinkedList();//队列用来存储所有还没有拿出来去扩展的子集
        subset.add(new HashSet<>(begin_state));//最开始，对初态求闭包是第一个子集，对NFA来说，初态不唯一，但我设计的文法初态是唯一
        q.offer(new HashSet<>(begin_state));//将一个子集加入到Vector中时，也要记得加入到队列中
        while(q.size()>0)
        {
            HashSet<Integer> temp=q.poll();//弹出队首
            for(char c:nfa.finall)//对于每一个终结符a
            {
                if(c=='@')continue;
                HashSet<Integer> new_state;
                new_state=move(temp,c);
                getClosure(new_state);
                if(new_state.size()==0)continue;
                //接下来看一下得到的这个新状态是否真的是新的
                if(judge_new(new_state)==true)//如果是新的就把它加入到子集中,同时加入到队列中进行下一步更新
                {
                    subset.add(new HashSet<>(new_state));
                    q.offer(new HashSet<>(new_state));
                    //在求子集的同时就可以将dfa建立出来
                }
                int a=get_state(temp),b=get_state(new_state);//注意这里是从原态连向现在的状态
                //System.out.println(a+"  "+b);
                add(a,b,c);
            }
        }
    }
    void print_subset()
    {
        int i=0;
        for(HashSet<Integer> x:subset)
        {
            System.out.println("子集"+i+":");
            for(int y:x)
            {
                System.out.print("S["+y+"]  ");
            }
            //System.out.println("这是子集"+get_state(x));
            System.out.println("\n***************************");
            i++;
        }
    }
    void print_dfa()
    {
        for(int i=0;i<subset.size();i++)
        {
            for(int j=h[i];j!=-1;j=ne[j])
            {
                int k=e[j];
                System.out.println("状态"+i+"经过 "+(char)w[j]+" 转换到状态"+k);
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new DFA_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\wenfa.txt");

    }
}
