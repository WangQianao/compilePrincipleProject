package com.wqa.compiler.gui;

import com.wqa.compiler.lexical.Lexer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    public MainWindow()
    {
        setTitle("软件课程设计II");
        setVisible(true);
        setBounds(50,50,700,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton jButton1=new JButton("词法分析");
        JButton jButton2=new JButton("语法分析");
        Container container = getContentPane();
        container.setLayout(new GridLayout(1,2));
        jButton1.setSize(100,50);
        jButton2.setSize(100,50);
        container.add(jButton1);
        container.add(jButton2);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LexicalWindow();
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Lexer.lexer_result.size()>0)
                {
                    new GrammarWindow();
                }

            }
        });
    }

}
