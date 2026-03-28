package com.project;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JPanel {

    public LoginGUI(App app) {
        setLayout(new BorderLayout());

        // ใช้ BackgroundPanel ที่คุณมีอยู่แล้ว
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new GridBagLayout());

        // ส่วนของกล่อง Login
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(350, 250)); // ขยายขนาดเล็กน้อยให้พอดี
        // ปรับให้พื้นหลังโปร่งแสงเพื่อให้เห็นรูป BG
        loginPanel.setBackground(new Color(255, 255, 255, 200)); 
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createTitledBorder("System Login"));

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 40));

        // ===== LOGIC: เมื่อกด Login สำเร็จ ให้สลับหน้าไป HOME =====
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                JOptionPane.showMessageDialog(app, "Login สำเร็จแล้วค้าบ");
                // สั่งสลับหน้าไปยังหน้า HOME
                app.switchPage("HOME"); 
                
                // ล้างค่าในช่องกรอกเพื่อความปลอดภัย
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(app, "Username หรือ Password ไม่ถูกค้าบ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // จัดวาง Component ลงใน loginPanel
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);

        // ใส่ loginPanel ลงกลาง bgPanel
        bgPanel.add(loginPanel, new GridBagConstraints());

        // เพิ่ม bgPanel เข้าไปในตัว LoginGUI (JPanel หลัก)
        add(bgPanel, BorderLayout.CENTER);
    }
}