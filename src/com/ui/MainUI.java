package com.ui;

import com.strength.Estimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by lgluo on 2016/10/29.
 */
public class MainUI extends JFrame{
    private JTextField analysetxt;
    private JTextField strength;
    private JLabel strengthResult;
    private JButton generatebtn;

    private JPanel analysePanel;
    private JPanel generatePanel;
    private JPanel strengthPanel;

    private Test test = null;

    public MainUI() {
        super();
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        analysePanel = new JPanel();
        getContentPane().add(analysePanel, BorderLayout.NORTH);
        analysePanel.setBorder(BorderFactory.createTitledBorder("密码库分析"));

        JLabel generatelbl = new JLabel("文件名：");
        analysePanel.add(generatelbl);

        analysetxt = new JTextField();
        analysePanel.add(analysetxt);
        analysetxt.setColumns(10);

        JButton analysebtn = new JButton("密码分析");
        analysePanel.add(analysebtn);
        analysebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analysebtn.setEnabled(false);
                analysePassword();
                analysebtn.setEnabled(true);
            }
        });

        JLabel analyseNotice = new JLabel("（根据文件大小用时可能会不同）");
        analyseNotice.setForeground(Color.RED);
        analysePanel.add(analyseNotice);

        generatePanel = new JPanel();
        getContentPane().add(generatePanel, BorderLayout.CENTER);
        generatePanel.setBorder(BorderFactory.createTitledBorder("生成密码库"));

        generatebtn = new JButton("生成密码库");
        generatePanel.add(generatebtn);
        generatebtn.setEnabled(false);
        generatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(test != null) {
                    test.generateLibrary();
                    JOptionPane.showMessageDialog(generatePanel, "密码分析完成，请查看日志文件");
                } else {
                    JOptionPane.showMessageDialog(analysePanel, "请先完成密码分析！","错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        strengthPanel = new JPanel();
        getContentPane().add(strengthPanel, BorderLayout.SOUTH);
        strengthPanel.setBorder(BorderFactory.createTitledBorder("密码强度检测"));

        JLabel lblNewLabel = new JLabel("输入密码：");
        strengthPanel.add(lblNewLabel);

        strength = new JTextField();
        strengthPanel.add(strength);
        strength.setColumns(10);

        JButton strengthbtn = new JButton("密码强度检测");
        strengthPanel.add(strengthbtn);
        strengthbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strengthChecking();
            }
        });

        strengthResult = new JLabel("密码强度：");
        strengthResult.setForeground(Color.RED);
        strengthPanel.add(strengthResult);

    }

    private void analysePassword() {
        String filename = analysetxt.getText();
        if(filename == null || filename.length() > 30 || filename.equals("")) {
            JOptionPane.showMessageDialog(analysePanel, "输入文件错误！","错误", JOptionPane.ERROR_MESSAGE);
        } else {
            File file = new File(filename);
            if(file.exists()) {
                generatebtn.setEnabled(true);
                test = new Test();
                test.analyse(filename);
                JOptionPane.showMessageDialog(analysePanel, "密码分析完成，请查看日志文件");
            } else {
                JOptionPane.showMessageDialog(analysePanel, "文件不存在！","错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void strengthChecking() {
        String password = strength.getText();
        if(password == null || password.length() > 20 || password.equals("")) {
            JOptionPane.showMessageDialog(analysePanel, "输入密码格式错误！","错误", JOptionPane.ERROR_MESSAGE);
        } else {
            int score = 0;
            if(password.length() >= 6) {
                score = Estimation.getInstance().checkStrength(password);
            }
            strengthResult.setText("密码强度：" + String.valueOf(score));
        }
    }

    public static void main(String[] args) {
        MainUI ui = new MainUI();
        ui.show();
    }
}
