package com.project;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);
    private Home homePanel;
    private Wishlist wishlistPanel; // เพิ่มตัวแปรหน้า Wishlist

    public App() {
        setTitle("YET?");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. สร้าง Instance ของหน้าต่างๆ
        homePanel = new Home(this);
        LoginGUI loginPanel = new LoginGUI(this);
        Budget budgetPanel = new Budget(this);
        wishlistPanel = new Wishlist(this); // สร้างหน้า Wishlist

        // 2. แอดหน้าต่างๆ เข้าไปใน container และตั้งชื่อ (Key) ให้ถูกต้อง
        container.add(loginPanel, "LOGIN"); 
        container.add(homePanel, "HOME");
        container.add(budgetPanel, "BUDGET");
        container.add(wishlistPanel, "WISHLIST"); // แอดหน้า Wishlist เข้าไป

        add(container);

        // เริ่มต้นที่หน้า LOGIN
        cardLayout.show(container, "LOGIN");
    }

    // 3. ใช้เมธอด switchPage อันเดียวที่รองรับทุกหน้า
    public void switchPage(String name) {
        cardLayout.show(container, name);
        
        // ถ้ากลับไปหน้า HOME ให้รีเฟรชยอดเงินและประวัติ
        if (name.equals("HOME")) {
            homePanel.updateDisplay();
        }
        
        // ถ้าไปหน้า WISHLIST ให้สั่งรีเฟรชรายการของที่เล็งไว้
        if (name.equals("WISHLIST")) {
            wishlistPanel.updateList();
        }
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}