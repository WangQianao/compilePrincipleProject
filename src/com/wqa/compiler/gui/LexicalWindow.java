package com.wqa.compiler.gui;

import com.wqa.compiler.lexical.Lexer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LexicalWindow extends JFrame {
    File wenfaFile;
    File codeFile;
    public LexicalWindow(){

        Container container = getContentPane();
        container.setLayout(new GridLayout(1,2));

        JPanel upper_panel=new JPanel(new BorderLayout());
        JLabel wenfa_label=new JLabel("正规文法文件");
        upper_panel.add(wenfa_label,BorderLayout.WEST);
        JTextField jTextField1=new JTextField();
        upper_panel.add(jTextField1,BorderLayout.CENTER);
        JButton browse1=new JButton("Browse");

        browse1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(); //文件选择
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Allowed File","txt");//过滤可以选择的文件
                chooser.setFileFilter(filter);
                chooser.showOpenDialog(chooser);        //打开文件选择窗
                wenfaFile = chooser.getSelectedFile();  	//获取选择的文件
               if(wenfaFile!=null)jTextField1.setText(wenfaFile.getPath());
            }
        });

        upper_panel.add(browse1,BorderLayout.EAST);

        JPanel mid_panel=new JPanel(new BorderLayout());
        JLabel code_label=new JLabel("源代码文件");
        mid_panel.add(code_label,BorderLayout.WEST);
        JTextField jTextField2=new JTextField();
        mid_panel.add(jTextField2,BorderLayout.CENTER);
        JButton browse2=new JButton("Browse");

        browse2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(); //文件选择
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Allowed File","txt");//过滤可以选择的文件
                chooser.setFileFilter(filter);

                chooser.showOpenDialog(chooser);        //打开文件选择窗

                codeFile = chooser.getSelectedFile();  	//获取选择的文件
                if(codeFile!=null)jTextField2.setText(codeFile.getPath());
            }
        });
        mid_panel.add(browse2,BorderLayout.EAST);
        JTextArea jTextArea=new JTextArea(20,50);
        jTextArea.setText("hello!!");

        JButton begin_parser=new JButton("开始分析");
        begin_parser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wenfaFile!=null&&codeFile!=null)
                {
                    Lexer lexer = null;
                    try {
                        lexer = new Lexer(jTextField1.getText(),jTextField2.getText());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    String text="";
                    try {
                        text=lexer.Scan();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    jTextArea.setText(text);
                }

            }
        });

        JPanel jPanel1=new JPanel();
        jPanel1.setLayout(new GridLayout(3,1));
        jPanel1.add(upper_panel);
        jPanel1.add(mid_panel);
        JScrollPane jScrollPane=new JScrollPane(jTextArea);

        jPanel1.add(begin_parser);
        container.add(jPanel1);
        container.add(jScrollPane);

        setTitle("词法分析");
        setVisible(true);
        pack();
        setBounds(50,50,700,700);
    }

}
