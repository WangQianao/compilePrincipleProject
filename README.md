# 项目说明
## 词法分析
程序有两个输入：一个文本文档，包括一组 3º型文法（正规文法）的产生式；一个源代码文本文档，包含一组需要识别的字符串（程序代码）。 程序的输出是一个 token（令牌）表，该表由 5 种 token 组成：关键词，标识符，常量，限定符和运算符。
词法分析程序的处理逻辑：根据用户输入的正规文法，生成 NFA，再确定化生成 DFA，根据 DFA 编写识别 token 的程序，从头到尾从左至右识别用户输入的源代码，生成 token 列表（三元组：所在行号，类别，token 内容）。该程序可以准确识别:科学计数法形式的常量（如 0.314E+1），复数常量（如10+12i），可检查整数常量的合法性，标识符的合法性（首字符不能为数字等）。
## 语法分析
使用 LR(1) 方法进行语法分析。程序有两个输入：1）一个是文本文档，其中包含 2º型文法（上下文无关文法）的产生式集
合；2）词法分析程序输出的（生成的）token 令牌表。程序的输出包括：YES 或 NO。源代码字符串符合此 2º型文法，或者源代码字符串不符合此 2º型文法）；错误提示文件，并给出大致的出错原因。
语法分析程序的处理逻辑：根据用户输入的 2º型文法，生成 Action 及 Goto 表，设计合适的数据结构，判断 token 序列（用户输入的源程序转换）。能演示语法处理的中间过程。
## 图形化界面
为上面的词法分析和语法分析封装一个图形化界面，增加与用户的交互

# 源代码文件结构

├─Main.java
├─lexical
|    ├─code.txt
|    ├─DFA_.java
|    ├─Identifier.java
|    ├─Lexer.java
|    ├─NFA_.java
|    ├─test.txt
|    ├─Token.java
|    └wenfa.txt
├─gui
|  ├─GrammarWindow.java
|  ├─LexicalWindow.java
|  └MainWindow.java
├─grammar
|    ├─First_.java
|    ├─Grammar_.java
|    ├─Parser_.java
|    ├─Set_.java
|    ├─yufa.txt
|    ├─yufa_design.txt
|    └终结符对照表.txt

src文件夹下，是项目的源代码

Main类，整个项目的入口<br>
<hr>
gui包下是与图形化界面编程相关的代码<br>

MainWindow：主窗口类<br>
LexicalWindow：词法分析窗口类<br>
GrammarWindow：语法分析窗口类<br>
resultFrame：语法分析细节展示窗口类<br>
<hr>
lexical包下是词法分析相关的部分<br>

NFA_类：实现NFA的表示与相关处理<br>
DFA_类：实现DFA的表示与相关处理<br>
Lexer类：词法分析主类<br>
Identifier类：辅助类<br>
Token类：三元组表示类<br>
code.txt:源代码文件<br>
wenfa.txt:正规文法文件<br>

<hr>
grammar包下是与语法分析相关的部分：<br>

Grammar_类：处理与存储上下文无关文法相关逻辑的类<br>
First_类：处理与存储First集合相关逻辑的类<br>
Set_类：得到项目集族和ACTION表<br>
Parser_类：语法相关主类<br>
yufa.txt：上下文无关文法<br>
yufa_design.txt：语法设计思路<br>
终结符对照表.txt：将设计思路中的中文转换为字符的对照表<br>
<hr>
图形化界面和词法分析与语法分析的实现是低耦合的，也就是说，如果不需要图形化界面了，可以直接将图形化界面去掉，剩下的部分依然可以正常运行。

