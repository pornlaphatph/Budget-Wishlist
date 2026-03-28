package com.project;

import javax.swing.*;
import java.awt.*;

public class Wishlist extends JPanel {
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> wishlistDisplay = new JList<>(listModel);
    
    // ตั้งค่าฟอนต์มาตรฐาน
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

    public Wishlist(App app) {
        setLayout(new BorderLayout());

        // --- ส่วนหัว (Header) ---
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

        // --- ส่วนแสดงรายการ (Center) ---
        wishlistDisplay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(wishlistDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // --- ส่วนปุ่มจัดการ (Bottom) ---
        JPanel bottomPanel = new JPanel();
        
        JButton btnAdd = new JButton("เพิ่มรายการ");
        JButton btnAnalyze = new JButton("วิเคราะห์ & ซื้อ"); // ปุ่มใหม่ที่รวมร่างแล้ว
        JButton btnDelete = new JButton("ลบรายการ");

        // เซตฟอนต์ให้ปุ่ม
        btnAdd.setFont(thaiFont);
        btnAnalyze.setFont(thaiFont);
        btnDelete.setFont(thaiFont);

        // Logic: เพิ่มรายการใหม่
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
                    JOptionPane.showMessageDialog(this, "กรุณากรอกราคาเป็นตัวเลขจ้า", "ผิดพลาด", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Logic: วิเคราะห์ความคุ้มค่าและตัดสินใจซื้อ (แทนที่ปุ่มซื้อเดิม)
        btnAnalyze.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                String name = DataStore.wishNames.get(index);
                double price = DataStore.wishPrices.get(index);

                String input = JOptionPane.showInputDialog(this, "คุณคาดว่าจะใช้งาน '" + name + "' ประมาณกี่ครั้ง?");
                if (input != null && !input.isEmpty()) {
                    try {
                        int times = Integer.parseInt(input);
                        double costPerTime = price / times;
                        
                        String resultMsg = String.format("ราคาต่อการใช้ 1 ครั้ง: ฿%.2f\n", costPerTime);
                        String decision = (costPerTime <= 50) ? "คุ้มมาก! ซื้อเลยมั้ย?" : "แอบแพงนะ... จะซื้อจริงหรอ?";

                        int finalChoice = JOptionPane.showConfirmDialog(this, resultMsg + decision, "วิเคราะห์ความคุ้มค่า", JOptionPane.YES_NO_OPTION);
                        
                        if (finalChoice == JOptionPane.YES_OPTION) {
                            if (DataStore.currentBalance >= price) {
                                DataStore.currentBalance -= price;
                                DataStore.history.add("- [Wishlist] " + name + " (฿" + price + ")");
                                DataStore.wishNames.remove(index);
                                DataStore.wishPrices.remove(index);
                                updateList();
                                JOptionPane.showMessageDialog(this, "ซื้อสำเร็จ! เหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));
                            } else {
                                JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า! เก็บออมเพิ่มก่อนนะ", "ยอดเงินไม่พอ", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "กรุณากรอกจำนวนครั้งเป็นตัวเลข", "ผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "เลือกรายการที่จะวิเคราะห์ก่อนนะ");
            }
        });

        // Logic: ลบรายการ
        btnDelete.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                DataStore.wishNames.remove(index);
                DataStore.wishPrices.remove(index);
                updateList();
            } else {
                JOptionPane.showMessageDialog(this, "เลือกรายการที่จะลบก่อนนะ");
            }
        });

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