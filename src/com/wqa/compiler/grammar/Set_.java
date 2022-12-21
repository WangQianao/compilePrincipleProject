package com.wqa.compiler.grammar;

import java.io.IOException;
import java.util.*;
import java.util.Set;

public class Set_ {
    public static String set_str="";
    public static String action_str="";
    First_ first_;
    final int N=300;
    String action_table[][];

    class Project
    {
        public int index;
        public int point;
        HashSet<Character> search=new HashSet<>();
        public Project(Project p)
        {
            this.index=p.index;
            this.point=p.point;
            this.search=new HashSet<>(p.search);
        }
        public Project()
        {

        }
    }
    Vector<Vector<Project>> program;//项目集
    public Set_(String grammarFile) throws IOException {
        program=new Vector<>();
        first_=new First_(grammarFile);
        action_table=new String[N][N];
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
                action_table[i][j]=" ";
            }
        }
        get_program();
        get_action_table();
        print_program();
        print_action_table();
    }
    HashSet<Character> get_search(String a,HashSet<Character> search)//求一个向前搜索符
    {
        //这里等价于对a这个字符串求一下first集合
        HashSet<Character> res=new HashSet<>();
        int i=0;
        for(i=0;i<a.length();i++)
        {
            char b=a.charAt(i);
            if(Character.isUpperCase(b)==true)
            {
                boolean flag=false;
                for(char c:first_.first_set.get(b))
                {
                    if(c=='@')
                    {
                        flag=true;
                        continue;
                    }
                    res.add(c);
                }
                if(flag==false)break;

            }else
            {
                res.add(b);
                break;
            }
        }
        if(i==a.length())
        {
            for(char c:search)res.add(c);
        }

        return res;
    }
    boolean is_contained_project(Vector<Project> temp,Project p)
    {
        for(Project x:temp)
        {
            if(x.point!=p.point)continue;
            if(x.index!=p.index)continue;
            if(x.search.size()!=p.search.size())continue;
            boolean flag=true;
            for(char a:x.search)
            {
                if(p.search.contains(a)==false)
                {
                    flag=false;
                    break;
                }
            }
            if(flag==true)return true;
        }
        return false;
    }
    void get_closure(Vector<Project> temp)
    {
        boolean haschange=true;
        while(haschange)
        {
            haschange=false;
            //System.out.println(temp.size());
            Vector<Project> temp2=new Vector<>();
            for(Project x:temp)
            {
                String wenfa=first_.grammar_.grammar[x.index];
                if(2+x.point==wenfa.length())continue;
                char B=wenfa.charAt(2+x.point);
                if(Character.isUpperCase(B))
                {
                    String a=wenfa.substring(2+x.point+1);
                    HashSet<Character> search=get_search(a,x.search);
                    for(int i=0;i<=first_.grammar_.count;i++)
                    {
                        if(first_.grammar_.grammar[i].charAt(0)==B)
                        {
                            Project p=new Project();
                            p.point=1;
                            p.search=search;
                            p.index=i;
                            if(is_contained_project(temp,p)==false&&is_contained_project(temp2,p)==false)
                            {
                                haschange=true;
                                temp2.add(p);
                            }
                        }
                    }
                }
            }
            for(Project x:temp2)
            {
                temp.add(x);
            }
        }
    }
    public  boolean is_contained_program(Vector<Project> temp) {

        for(Vector<Project> x:program)
        {

            if(x.size()!=temp.size())continue;
            boolean flag=true;
            for(Project p:x)
            {
                if(is_contained_project(temp,p)==false)
                {
                    flag=false;
                    break;
                }
            }
            if(flag==true)return true;
        }
        return false;
    }
    int get_index(Vector<Project> temp)
    {
        int i=0;
        for(Vector<Project> x:program)
        {
            if(x.size()!=temp.size())
            {
                i++;
                continue;
            }
            boolean flag=true;
            for(Project p:x)
            {
                if(is_contained_project(temp,p)==false)
                {
                    flag=false;
                    break;
                }
            }
            if(flag==true)return i;
            i++;
        }
        return -1;
    }
    void move(char Vn,Vector<Project> temp,Queue<Vector<Project>> q)
    {
        Vector<Project> temp2=new Vector<>();
        for(Project x:temp)
        {
            String wenfa=first_.grammar_.grammar[x.index];
            if(x.point+2==wenfa.length())continue;
            char a=wenfa.charAt(x.point+2);
            if(a!=Vn)continue;
            Project y=new Project();
            y.index=x.index;
            y.search=x.search;
            y.point=x.point+1;
            if(is_contained_project(temp2,y)==false)
            {
                temp2.add(new Project(y));
            }
        }
        if(temp2.size()!=0)
        {
            get_closure(temp2);

            if(is_contained_program(temp2)==false)
            {
                program.add(new Vector<>(temp2));
                q.offer(new Vector<>(temp2));
            }
            int a=get_index(temp);
            int b=get_index(temp2);
            if(Character.isUpperCase(Vn)==true)
            {
                action_table[a][Vn]=""+b;
            }else
            {
                action_table[a][Vn]="S"+b;
            }
        }
    }
    void get_program()
    {
        Vector<Project> begin_program=new Vector<>();
        Project begin_project=new Project();
        begin_project.index=0;
        begin_project.point=1;
        begin_project.search.add('#');//初始项目 S’→•S ,#
        begin_program.add(new Project(begin_project));
        get_closure(begin_program);
        Queue<Vector<Project>> q=new LinkedList();
        q.offer(new Vector<>(begin_program));
        program.add(new Vector<>(begin_program));
        while(q.size()>0)
        {
            Vector<Project> temp=q.poll();
            for(char Vn:first_.grammar_.state)
            {
                move(Vn,temp,q);
            }
            for(char Vt:first_.grammar_.finall)
            {
                if(Vt=='@')continue;
               move(Vt,temp,q);
            }
        }
    }
    void get_action_table()
    {
        int i=0;
        for(Vector<Project> x:program)
        {
            for(Project p:x)
            {
                String wenfa=first_.grammar_.grammar[p.index];
                //F->@,1,p
                if(wenfa.length()==2+p.point||wenfa.length()==4&&wenfa.charAt(3)=='@')//表示这是一个规约项目
                {
                    int a=i;
                    int b=p.index;
                    for(char c:p.search)
                    {
                        action_table[a][c]="r"+b;
                    }
                    if(wenfa.charAt(0)=='S'&&p.search.contains('#'))
                    {
                        action_table[a]['#']="acc";
                    }
                }
            }
            i++;
        }
    }
    void print_program()
    {
        //S’•S
        //左部(已识别部分)  •  右部(待识别部分)
        int i=0;
        for(Vector<Project> x:program)
        {
            set_str+="I"+i+"\n";
            //System.out.println("I"+i);
            int t=get_index(x);
            for(Project p:x)
            {
                String wenfa=first_.grammar_.grammar[p.index];
                for(int j=0;j<wenfa.length();j++)
                {
                    if(j==p.point+2)set_str+="•";
                    set_str+=wenfa.charAt(j);
                }
                if(p.point+2==wenfa.length())set_str+="•";
                set_str+=",";
                //System.out.print(first_.grammar_.grammar[p.index]+","+p.point+",");
                int j=0;
                for(char c:p.search)
                {
                    set_str+=c;
                    //System.out.print(c);
                    j++;
                    if(j<p.search.size()) set_str+="/";//System.out.print("/");
                }
                set_str+="\n";
                //System.out.println();
            }
            set_str+="------------------------------------------------\n";
            //System.out.println("-------------"+t+"--------------------");
            i++;
        }
    }
    void print_action_table()
    {
        int blk=7;
        String temp="";
        temp+="状态";
        while(temp.length()<blk)temp+=" ";
        action_str+=temp;
        //System.out.print(temp);
        temp="";
        for(char c:first_.grammar_.finall)
        {
            if(c=='@')continue;
            temp+=c;
            while(temp.length()<blk)temp+=" ";
            //System.out.print(temp);
            action_str+=temp;
            temp="";
        }
        for(char c:first_.grammar_.state)
        {
            temp+=c;
            while(temp.length()<blk)temp+=" ";
            action_str+=temp;
            //System.out.print(temp);
            temp="";
        }
        action_str+="\n";
        //System.out.println();
        for(int i=0;i<350;i++) action_str+="-";//System.out.print("-");
        action_str+="\n";
        //System.out.println();
        for(int i=0;i<program.size();i++)
        {
            temp+=i;
            while(temp.length()<blk)temp+=" ";
            action_str+=temp;
            //System.out.print(temp);
            temp="";
            for(char c:first_.grammar_.finall)
            {
                if(c=='@')continue;
                temp+=action_table[i][c];
                while(temp.length()<blk)temp+=" ";
                //System.out.print(temp);
                action_str+=temp;
                temp="";
            }
            for(char c:first_.grammar_.state)
            {
                temp+=action_table[i][c];
                while(temp.length()<blk)temp+=" ";
                //System.out.print(temp);
                action_str+=temp;
                temp="";
            }
            //System.out.println();
            action_str+="\n";
            for(int j=0;j<350;j++) action_str+="-";//System.out.print("-");
            action_str+="\n";
            //System.out.println();
        }
    }
    public static void main(String[] args) throws IOException {
        new Set_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\grammar\\yufa.txt");
        System.out.println(set_str);
        System.out.println(action_str);

    }
}
