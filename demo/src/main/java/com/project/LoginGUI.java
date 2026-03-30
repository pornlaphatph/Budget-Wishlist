package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JPanel {

    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI(App app) {
        setLayout(new BorderLayout());

        UIManager.put("TextField.font", thaiFont);
        UIManager.put("PasswordField.font", thaiFont);

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new GridBagLayout());

        // ส่วนของกล่อง Login
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(350, 250));
        loginPanel.setBackground(new Color(255, 255, 255, 200)); 
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createTitledBorder("System Login"));

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 40));

        
        ActionListener loginAction = e -> performLogin(app);

        
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
        
        
        loginButton.addActionListener(loginAction);

        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);

        bgPanel.add(loginPanel, new GridBagConstraints());
        add(bgPanel, BorderLayout.CENTER);
        
        
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    
    private void performLogin(App app) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("a") && password.equals("1")) {
            JOptionPane.showMessageDialog(app, "Login สำเร็จแล้วค้าบ");
            app.switchPage("HOME"); 
            
            // ล้างค่าในช่องกรอก
            usernameField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(app, "Username หรือ Password ไม่ถูกค้าบ", "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        }
    }
}