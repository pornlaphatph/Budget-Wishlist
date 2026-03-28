package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI {
    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont" , new Font("Tahoma" , Font.PLAIN, 14));

      SwingUtilities.invokeLater(() -> {
       
      JFrame frame = new JFrame("Login System");
      frame.setSize(600,400);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);

      BackgroundPanel bgPanel = new BackgroundPanel();
      bgPanel.setLayout(new GridBagLayout());

      JPanel loginPanel = new JPanel();
      loginPanel.setPreferredSize(new Dimension(300,180));
      loginPanel.setBackground(new Color(0, 0, 0, 0));
      loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
      loginPanel.setBorder(BorderFactory.createEmptyBorder(27, 27, 27, 27));

      JTextField usernameField = new JTextField();
      usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
      usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

      JPasswordField passwordField = new JPasswordField();
      passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
      passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

      JButton loginButton = new JButton("Login");

      loginButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.equals("admin") && password.equals("1234")) {
            JOptionPane.showMessageDialog(frame, "Login สำเร็จแล้วค้าบ");
        } else {
            JOptionPane.showMessageDialog(frame, "Username หรือ Password ไม่ถูกค้าบ");
        }
    });

        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);

        bgPanel.add(loginPanel, new GridBagConstraints());

        frame.setContentPane(bgPanel);
        frame.setVisible(true);
    });
  }
}