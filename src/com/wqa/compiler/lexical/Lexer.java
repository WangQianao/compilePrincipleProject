package com.wqa.compiler.lexical;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;

public class Lexer {

    public static Vector<Token> lexer_result;
    private final String codeFile;
    public boolean haserror=false;
    private DFA_ dfa;
    public static int output_row[];
    public Lexer(String lexicalGrammar,String codeFile) throws IOException {
        this.codeFile=codeFile;
        dfa=new DFA_(lexicalGrammar);
        output_row=new int[300];
        lexer_result=new Vector<>();
    }

    String judge(String str)//判断字符串str是什么类型
    {
        Identifier identifier=new Identifier();
        String res="";
        if(identifier.IsKeyword(str))
        {
            res+="关键词";
        }
        else if(identifier.binary_op.contains(str))
        {
            res+="双目运算符";
        }
        else if(str.length()==1&&identifier.delimiter.contains(str.charAt(0)))
        {
            res+="限定符";
        }
        else if(str.length()==1&&identifier.unary_op.contains(str.charAt(0)))
        {
            res+="单目运算符";
        }
        else if(str.charAt(0)=='_'||Character.isLetter(str.charAt(0)))
        {
            res+="标识符";
        }else
        {
            res+="常量";
        }
        return res;
    }
    public String Scan() throws IOException {
        String str="";
        char ch;
        String code;
        int row=0;
        BufferedReader in = new BufferedReader( new java.io.FileReader(codeFile));
        String res="";
        String last_type="";
        int p=0;
        int last=0;
        int index=0;
        while ((code = in.readLine()) != null) {
            row++;
            if(code.length()==0)continue;
            str="";
            for(int i=0;i<code.length();i++) {
                ch = code.charAt(i);
                if (ch == ' ' || ch == '\t') {
                    continue;
                } else str += ch;
            }
            last=0;
            last_type="";
            for(int i=0;i<str.length();i++)
            {
                ch=str.charAt(i);
                boolean flag=false;
                for(int j=dfa.h[p];j!=-1;j=dfa.ne[j])
                {
                    int k=dfa.e[j];
                    if((char)dfa.w[j]==ch)
                    {
                        flag=true;
                        p=k;
                        break;
                    }
                }
                if(flag==false)
                {
                    if(dfa.is_final_state(p))//表明识别到了一个token
                    {
                        String token=str.substring(last,i);
                        String type=judge(token);
                        output_row[index++]=row;
                        if(type.equals("标识符")&&last_type.equals("常量"))
                        {
                            res="";
                            res+="第"+row+"行[Error],标识符不能以数字开头\n";
                            System.out.println("[Error]标识符不能以数字开头");
                            haserror=true;
                            break;
                        }else
                        {
                            System.out.println("("+row+","+type+","+token+")");
                            lexer_result.add(new Token(row,type,token));
                            res+="("+row+","+type+","+token+")\n";
                        }
                        last_type=type;
                        last=i;
                        p=0;
                        i--;
                    }else
                    {
                        res="";
                        res+="第"+row+"行[Error]can not identify the token!\n";
                        System.out.println("[Error]can not identify the token!");
                        haserror=true;
                        break;
                    }
                }
            }
            if(haserror==true)break;
            //退出循环时，句尾最后一个token可能没有被识别
            if(p!=0)
            {
                if(dfa.is_final_state(p))//表明识别到了一个token
                {
                    String token=str.substring(last);
                    String type=judge(token);
                    output_row[index++]=row;
                    if(type.equals("标识符")&&last_type.equals("常量"))
                    {
                        res="";
                        res+="第"+row+"行[Error],标识符不能以数字开头\n";
                        System.out.println("[Error],标识符不能以数字开头");
                        haserror=true;
                        break;
                    }else
                    {
                        System.out.println("("+row+","+type+","+token+")");
                        res+="("+row+","+type+","+token+")\n";
                        lexer_result.add(new Token(row,type,token));
                    }
                    p=0;
                }
            }
            if(haserror==true)break;
        }

        output_row[index++]=row;
        if(haserror==true)lexer_result.clear();
        return res;
    }
    void print_token()
    {
        for(Token t:lexer_result)
        {
            System.out.println(t);
        }
    }
    public static void main(String[] args) throws IOException {
        Lexer lexer_=new Lexer("C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\wenfa.txt",
                "C:\\Users\\WangQianao\\Desktop\\compilePrinciple\\src\\com\\wqa\\compiler\\lexical\\code.txt");
        lexer_.Scan();
        lexer_.print_token();


    }

}
