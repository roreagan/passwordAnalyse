package com.ui;

import com.generate.GenerateByInfo;
import com.strength.Estimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * Created by lgluo on 2016/10/29.
 */
public class MainUI extends JFrame{
    private JTextField analysetxt;
    private JTextField strength;
    private JTextField txt_firstName;
    private JTextField txt_lastName;
    private JTextField txt_date;
    private JTextField txt_phone;
    private JTextField txt_other;

    private JCheckBox basedOnInput;
    private JLabel strengthResult;
    private JButton generatebtn;

    private JPanel analysePanel;
    private JPanel generatePanel;
    private JPanel strengthPanel;

    private Test test = null;

    public MainUI() {
        super();
        setSize(500, 350);
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
        GridBagLayout gblGenerate = new GridBagLayout();
        gblGenerate.columnWidths = new int[]{0, 0, 0, 0, 0};
        gblGenerate.rowHeights = new int[]{0, 0, 0, 0, 0};
        gblGenerate.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gblGenerate.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        generatePanel.setLayout(gblGenerate);

        JLabel lbl_firstName = new JLabel("姓（字母表示）：");
        GridBagConstraints gbc_firstName = new GridBagConstraints();
        gbc_firstName.insets = new Insets(0, 0, 5, 0);
        gbc_firstName.gridx = 0;
        gbc_firstName.gridy = 0;
        generatePanel.add(lbl_firstName, gbc_firstName);

        txt_firstName = new JTextField();
        GridBagConstraints gbc_txt_firstName = new GridBagConstraints();
        gbc_txt_firstName.insets = new Insets(0, 0, 5, 0);
        gbc_txt_firstName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txt_firstName.gridx = 1;
        gbc_txt_firstName.gridy = 0;
        generatePanel.add(txt_firstName, gbc_txt_firstName);

        JLabel lbl_lastName = new JLabel("名（字母表示）：");
        GridBagConstraints gbc_lastName = new GridBagConstraints();
        gbc_lastName.insets = new Insets(0, 5, 5, 0);
        gbc_lastName.gridx = 2;
        gbc_lastName.gridy = 0;
        generatePanel.add(lbl_lastName, gbc_lastName);

        txt_lastName = new JTextField();
        GridBagConstraints gbc_txt_lastName = new GridBagConstraints();
        gbc_txt_lastName.insets = new Insets(0, 0, 5, 0);
        gbc_txt_lastName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txt_lastName.gridx = 3;
        gbc_txt_lastName.gridy = 0;
        generatePanel.add(txt_lastName, gbc_txt_lastName);

        JLabel lbl_date = new JLabel("日期（20161230）：");
        GridBagConstraints gbc_date = new GridBagConstraints();
        gbc_date.insets = new Insets(0, 0, 5, 0);
        gbc_date.gridx = 0;
        gbc_date.gridy = 1;
        generatePanel.add(lbl_date, gbc_date);

        txt_date = new JTextField();
        GridBagConstraints gbc_txt_date = new GridBagConstraints();
        gbc_txt_date.insets = new Insets(0, 0, 5, 0);
        gbc_txt_date.fill = GridBagConstraints.HORIZONTAL;
        gbc_txt_date.gridx = 1;
        gbc_txt_date.gridy = 1;
        generatePanel.add(txt_date, gbc_txt_date);

        JLabel lbl_phtone = new JLabel("手机号：");
        GridBagConstraints gbc_phone = new GridBagConstraints();
        gbc_phone.insets = new Insets(0, 0, 5, 0);
        gbc_phone.gridx = 0;
        gbc_phone.gridy = 2;
        generatePanel.add(lbl_phtone, gbc_phone);

        txt_phone = new JTextField();
        GridBagConstraints gbc_txt_phone = new GridBagConstraints();
        gbc_txt_phone.insets = new Insets(0, 0, 5, 0);
        gbc_txt_phone.fill = GridBagConstraints.HORIZONTAL;
        gbc_txt_phone.gridx = 1;
        gbc_txt_phone.gridy = 2;
        generatePanel.add(txt_phone, gbc_txt_phone);

        JLabel lbl_other = new JLabel("其它可能信息：");
        GridBagConstraints gbc_other = new GridBagConstraints();
        gbc_other.insets = new Insets(0, 0, 5, 0);
        gbc_other.gridx = 0;
        gbc_other.gridy = 3;
        generatePanel.add(lbl_other, gbc_other);

        txt_other = new JTextField();
        GridBagConstraints gbc_txt_other = new GridBagConstraints();
        gbc_txt_other.insets = new Insets(0, 0, 5, 0);
        gbc_txt_other.fill = GridBagConstraints.HORIZONTAL;
        gbc_txt_other.gridx = 1;
        gbc_txt_other.gridy = 3;
        generatePanel.add(txt_other, gbc_txt_other);

        basedOnInput = new JCheckBox("根据输入信息");
        GridBagConstraints gbc_basedoninput = new GridBagConstraints();
        gbc_basedoninput.gridx = 0;
        gbc_basedoninput.gridy = 4;
        generatePanel.add(basedOnInput, gbc_basedoninput);
        basedOnInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(basedOnInput.isSelected()){
                    generatebtn.setEnabled(true);
                } else {
                    if(test == null){
                        generatebtn.setEnabled(false);
                    }
                }
            }
        });


        generatebtn = new JButton("生成密码库");
        generatebtn.setEnabled(false);
        generatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(basedOnInput.isSelected()) {
                    String info = "@" + txt_date.getText() + "---@" + txt_firstName.getText() + "---@" + txt_lastName.getText() + "---@" + txt_phone.getText() + "---@" + txt_other.getText();
                    if(GenerateByInfo.getInstance().generate(info) && txt_date.getText().length() == 8) {
                        JOptionPane.showMessageDialog(generatePanel, "已生成密码库，请查看txt文件！");
                    } else {
                        JOptionPane.showMessageDialog(analysePanel, "输入错误！","错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if(test != null) {
                        test.generateLibrary();
                        JOptionPane.showMessageDialog(generatePanel, "已生成密码库，请查看libByInput.txt文件！");
                    } else {
                        JOptionPane.showMessageDialog(analysePanel, "请先完成密码分析！","错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        GridBagConstraints gbc_generatebtn = new GridBagConstraints();
        gbc_generatebtn.gridx = 2;
        gbc_generatebtn.gridy = 4;
        generatePanel.add(generatebtn, gbc_generatebtn);

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
