package com.wqa.compiler.gui;

import com.wqa.compiler.grammar.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GrammarWindow extends JFrame {
    File wenfaFile;
    Parser_ parser_;
    public GrammarWindow(){
        Container container = getContentPane();
        container.setLayout(new GridLayout(3,1));
        Panel upper_panel=new Panel();
        upper_panel.setLayout(new BorderLayout());
        JLabel wenfa_label=new JLabel("上下文无关文法文件");
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


        Panel mid_panel=new Panel();
        mid_panel.setLayout(new BorderLayout());
        JButton parse_btn=new JButton("分析");

        JTextField result_field=new JTextField();
        mid_panel.add(result_field,BorderLayout.CENTER);
        JLabel result_label=new JLabel("结果");
        mid_panel.add(result_label,BorderLayout.EAST);
        parse_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    parser_=new Parser_(jTextField1.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    parser_.judge();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(Parser_.is_acc==true)
                {
                    result_field.setText("yes");
                }else
                {
                    result_field.setText("no");
                }
            }
        });
        mid_panel.add(parse_btn,BorderLayout.WEST);


        Panel lower_panel=new Panel();
        lower_panel.setLayout(new GridLayout(3,3));
        JButton first_btn=new JButton("查看First集合");
        first_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new resultFrame(First_.first_str);
            }
        });

        JButton wenfa_btn=new JButton("查看上下文无关文法");
        wenfa_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new resultFrame(Grammar_.grammar_str);
            }
        });
        JButton error_btn=new JButton("查看出错原因");
        error_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new resultFrame(Parser_.error_str);
            }
        });
        JButton set_btn=new JButton("查看LR1状态集族");
        set_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new resultFrame(Set_.set_str);
            }
        });
        JButton action_btn=new JButton("查看LR1分析表");
        action_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new resultFrame(Set_.action_str);

            }
        });
        JButton process_btn=new JButton("查看分析过程");
        process_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new resultFrame(Parser_.process_str);
            }
        });
        lower_panel.add(first_btn);
        lower_panel.add(wenfa_btn);
        lower_panel.add(error_btn);
        lower_panel.add(set_btn);
        lower_panel.add(action_btn);
        lower_panel.add(process_btn);
        container.add(upper_panel);
        container.add(mid_panel);
        container.add(lower_panel);
        setTitle("语法分析");
        setVisible(true);
        pack();
        setBounds(50,50,700,700);
    }
}
class resultFrame extends JFrame
{
    public resultFrame(){}
    public resultFrame(String str)
    {
        Container container = getContentPane();
        JTextArea jTextArea=new JTextArea();
        jTextArea.setText(str);
        JScrollPane jScrollPane=new JScrollPane(jTextArea);
        container.add(jScrollPane);

        setTitle("语法分析");
        setVisible(true);
        pack();
        setBounds(50,50,700,700);

    }
}
