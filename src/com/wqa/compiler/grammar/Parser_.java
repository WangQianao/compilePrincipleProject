package com.wqa.compiler.grammar;
import com.wqa.compiler.lexical.Identifier;
import com.wqa.compiler.lexical.Lexer;
import com.wqa.compiler.lexical.Token;

import java.io.IOException;
import java.util.Stack;

public class Parser_ {

    Stack<Integer> state_stack;
    Stack<Character> sign_stack;
    public static String error_str="";
    public static String process_str="";
    char buffer[];
    public Set_ lr1;
    int size=0;
    public static boolean is_acc;
    public Parser_(String grammar_file) throws  IOException {
        buffer = new char[300];
        lr1 = new Set_(grammar_file);
        state_stack = new Stack<Integer>();
        sign_stack = new Stack<Character>();
    }
    void write_stack(int x, String temp) throws IOException//输出状态栈和符号栈的情况
    {
        //思路就是先将符号栈或者状态栈中的所有符号都弹出来，用另一个栈记录，然后再放回去
        Stack<Integer> a=new Stack<Integer>();
        Stack<Character> c=new Stack<Character>();
        temp="";
        int t=0;
        if(x==1)    //状态
        {
            while(state_stack.empty()==false)
            {
                a.push(state_stack.peek());
                state_stack.pop();
            }
            while(a.empty()==false)
            {
                temp+=a.peek()+",";
                t++;
                state_stack.push(a.peek());
                a.pop();
            }
        }
        if(x==2)   //符号
        {
            while(sign_stack.empty()==false)
            {
                c.push(sign_stack.peek());
                sign_stack.pop();
            }
            while(c.empty()==false)
            {
                //bufferedWriter.write(c.peek()+",");
                temp+=c.peek()+",";
                t++;
                sign_stack.push(c.peek());
                c.pop();
            }
        }
        printBlank(temp,110);
    }
    public void printBlank(String temp,int num) throws IOException {
        while(temp.length()<num)
        {
            temp+=" ";
        }
        process_str+=temp;
    }
    public String convert(Token t)
    {
        String type=t.type;
        Identifier identifier=new Identifier();
        if(type.equals("关键词"))
        {
            return identifier.f(t.content);
        }
        else if(type.equals("双目运算符"))
        {
            return "3";
        }else if(type.equals("限定符"))
        {
            return t.content;
        }else if(type.equals("单目运算符"))
        {
            char a=t.content.charAt(0);
            if(a=='='||a=='<'||a=='>')return t.content;
            else return "4";
        }else if(type.equals("标识符"))
        {
            return "1";
        }else
        {
            return "2";
        }
    }
    public void judge() throws IOException {
        size = 0;
        for(Token t:Lexer.lexer_result)
        {
            buffer[size]=convert(t).charAt(0);
            size++;
        }
        buffer[size++]='#';
        int    work_sta = 0;
        int    index_buf = 0;
        char now_in;
        now_in=buffer[0];
        state_stack.push(0);
        sign_stack.push('#');
        String temp="";
        temp+="状态栈";
        printBlank(temp,108);
        temp="";
        temp+="符号栈";
        printBlank(temp,108);
        temp="";
        temp+="输入串";
        printBlank(temp,108);
        temp="";
        temp+="ACTION";
        printBlank(temp,108);
        temp="";
        temp+="GOTO";
        printBlank(temp,108);
        process_str+="\n";
        int i,k;
        int tp,len;
        int aa;
        //error发生，就是遇到空白了，当前状态跟输入串之间没有转移的途径
        //System.out.println(process_str);
        boolean   error=false;
        while(error==false)
        {
            work_sta = state_stack.peek();
            write_stack(1,temp);
            write_stack(2,temp);
            temp="";
            for(i=index_buf;i<size;++i)//输出当前输入符号串
            {
                temp+=buffer[i];
            }
            printBlank(temp,110);
            temp="";
            System.out.println("状态栈顶为"+work_sta+" 当前输入符号为"+now_in);
            String action=lr1.action_table[work_sta][now_in];
            System.out.println("动作 "+action);
            if(action.charAt(0)=='S')//移进
            {
                //符号栈更新，状态栈更新
                process_str+=action+"\n";
                //bufferedWriter.write("S"+lr1.action_table[work_sta][i].next_state+"\n");
                state_stack.push(Integer.parseInt(action.substring(1)));
                sign_stack.push(now_in);
                ++index_buf;
                now_in=buffer[index_buf];
            }else if(action.charAt(0)=='r')//规约
            {
                /*
                    		规约要进行的操作
							符号栈先出栈，再入栈，状态栈先出栈，再入栈,输入符号串不变
						*/
                //这里tp就是文法的编号，第tp个文法
                tp = Integer.parseInt(action.substring(1));
                printBlank(temp,110);
                String wenfa=lr1.first_.grammar_.grammar[tp];
                len=wenfa.length()-3;
                if(wenfa.charAt(3)=='@')
                {
                    --len;
                }
                //符号栈和状态站弹栈
                for(k = 0; k < len; ++k)
                {
                    state_stack.pop();
                    sign_stack.pop();
                }
                //符号栈入栈，入的就是文法的左部
                sign_stack.push(wenfa.charAt(0));
                aa=state_stack.peek();//状态栈栈顶
                //通过新入栈的这个非终结符来找到状态栈应该要入的状态，也就是GOTO操作
                int next_state=Integer.parseInt(lr1.action_table[aa][wenfa.charAt(0)]);
                state_stack.push(next_state);
                process_str+=next_state+"\n";
            }else if(action=="acc")//成功
            {
                process_str+="\n"+"YES\n";
                is_acc=true;
                //bufferedWriter.write("\n"+"YES\n");
                System.out.println("Yes");
                break;
            }else if(action==" ")
            {
                error=true;
            }

        }
        if(error==true)
        {
            is_acc=false;
            Identifier identifier=new Identifier();
            error_str+="NO\n";
            error_str+="出错行号为"+ Lexer.output_row[index_buf-1]+"\n";
            error_str+="出错原因可能是在  "+identifier.inf(buffer[index_buf-1]+"")+"  之后未找到：";
            System.out.println("NO");
            System.out.println("出错行号为"+ Lexer.output_row[index_buf-1]);
            System.out.print("出错原因可能是在  "+identifier.inf(buffer[index_buf-1]+"")+"  之后未找到：");
            for(char c:lr1.first_.grammar_.finall)
            {
                if(lr1.action_table[work_sta][c]!=" ")
                {
                    error_str+=identifier.inf(c+"") + " ";
                    System.out.print(identifier.inf(c+"") + " ");
                }
            }
            error_str+="\n";
            System.out.println();
        }
    }
    public static void main(String[] args) throws IOException {

        Lexer lexer=new Lexer("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\wenfa.txt",
                "C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\code.txt" );
        lexer.Scan();
        Parser_ parser_=new Parser_("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\grammar\\yufa.txt");
        parser_.judge();
    }

}
