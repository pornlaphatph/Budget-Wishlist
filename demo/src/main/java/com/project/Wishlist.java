package com.project;

import javax.swing.*;
import java.awt.*;

public class Wishlist extends JPanel {
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> wishlistDisplay = new JList<>(listModel);
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

    public Wishlist(App app) {
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(thaiFont);
        btnBack.addActionListener(e -> app.switchPage("HOME"));
        
        JLabel title = new JLabel("Wishlist (รายการที่อยากได้)");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        header.add(btnBack);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- Center ---
        wishlistDisplay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(wishlistDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel();
        
        // 1. ต้องสร้าง (New) ปุ่มทั้งหมดก่อนจะไปสั่งงานมัน
        JButton btnAdd = new JButton("เพิ่มรายการ");
        JButton btnAnalyze = new JButton("วิเคราะห์และซื้อ"); // เปลี่ยนชื่อปุ่มซื้อเป็นวิเคราะห์
        JButton btnDelete = new JButton("ลบรายการ");

        btnAdd.setFont(thaiFont);
        btnAnalyze.setFont(thaiFont);
        btnDelete.setFont(thaiFont);

        // 2. Logic: เพิ่มรายการ
        btnAdd.addActionListener(e -> {
            UIManager.put("TextField.font", thaiFont);
            String name = JOptionPane.showInputDialog(this, "ชื่อสินค้าที่อยากได้:");
            if (name != null && !name.isEmpty()) {
                try {
                    String priceStr = JOptionPane.showInputDialog(this, "ราคา (บาท):");
                    if (priceStr != null) {
                        double price = Double.parseDouble(priceStr);
                        DataStore.wishNames.add(name);
                        DataStore.wishPrices.add(price);
                        updateList();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "กรุณากรอกราคาเป็นตัวเลขจ้า");
                }
            }
        });

        // 3. Logic: วิเคราะห์ความคุ้มค่า (แทนที่ปุ่มซื้อเดิม)
        btnAnalyze.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                String name = DataStore.wishNames.get(index);
                double price = DataStore.wishPrices.get(index);
                double currentBalance = DataStore.currentBalance;

                String input = JOptionPane.showInputDialog(this, "คุณคาดว่าจะใช้งาน '" + name + "' ประมาณกี่ครั้ง?");
                if (input != null && !input.isEmpty()) {
                    try {
                        int times = Integer.parseInt(input);
                        double cpu = price / times; // Cost Per Use
                        
                        // ใช้ Logic ดึงสติเหมือนหน้า Budget
                        String advice;
                        if (price > currentBalance) {
                            advice = "เงินไม่พอซื้อครับ!";
                        } else if (times <= 1 && price > (currentBalance * 0.05)) {
                            advice = "ไม่คุ้ม! ใช้ครั้งเดียวแต่แพงไปนิด";
                        } else if (cpu <= 50) {
                            advice = "คุ้มมาก! เฉลี่ยครั้งละ ฿" + String.format("%.2f", cpu);
                        } else {
                            advice = "แอบแพงนะ... เฉลี่ยครั้งละ ฿" + String.format("%.2f", cpu);
                        }

                        int finalChoice = JOptionPane.showConfirmDialog(this, 
                            "Item: " + name + "\n" + advice + "\nยืนยันจะซื้อหรือไม่?", 
                            "วิเคราะห์ความคุ้มค่า", JOptionPane.YES_NO_OPTION);
                        
                        if (finalChoice == JOptionPane.YES_OPTION) {
                            if (currentBalance >= price) {
                                DataStore.currentBalance -= price;
                                DataStore.history.add("- [Wishlist] " + name + " (฿" + price + ")");
                                DataStore.wishNames.remove(index);
                                DataStore.wishPrices.remove(index);
                                updateList();
                                JOptionPane.showMessageDialog(this, "ซื้อสำเร็จ! เหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));
                            } else {
                                JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า!");
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขจ้า");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "เลือกรายการก่อนนะ");
            }
        });

        // 4. Logic: ลบรายการ
        btnDelete.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                DataStore.wishNames.remove(index);
                DataStore.wishPrices.remove(index);
                updateList();
            }
        });

        // แอดปุ่มลง Panel
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnAnalyze);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateList() {
        listModel.clear();
        for (int i = 0; i < DataStore.wishNames.size(); i++) {
            listModel.addElement((i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + String.format("%.2f", DataStore.wishPrices.get(i)));
        }
    }
}