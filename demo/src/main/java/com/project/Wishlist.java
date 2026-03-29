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
        
        JButton btnAdd = new JButton("เพิ่มรายการ");
        JButton btnAnalyze = new JButton("วิเคราะห์และซื้อ");
        JButton btnDelete = new JButton("ลบรายการ");

        btnAdd.setFont(thaiFont);
        btnAnalyze.setFont(thaiFont);
        btnDelete.setFont(thaiFont);

        // Logic: เพิ่มรายการ
        btnAdd.addActionListener(e -> {
            UIManager.put("TextField.font", thaiFont);
            String name = JOptionPane.showInputDialog(this, "ชื่อสินค้าที่อยากได้:");
            if (name != null && !name.isEmpty()) {
                try {
                    String priceStr = JOptionPane.showInputDialog(this, "ราคา (บาท):");
                    if (priceStr != null) {
                        double price = Double.parseDouble(priceStr);
                        DataStore.wishNames.add(name); // จะไม่ error แล้วเพราะ DataStore แก้เป็น ArrayList แล้ว
                        DataStore.wishPrices.add(price);
                        updateList();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "กรุณากรอกราคาเป็นตัวเลขจ้า");
                }
            }
        });

        // Logic: วิเคราะห์และซื้อ
        btnAnalyze.addActionListener(e -> {
            if (DataStore.currentBalance <= 0) {
                JOptionPane.showMessageDialog(this, "ตอนนี้ Budget เป็น 0 ค้าบ! ไปตั้งงบก่อนนะ", "Warning", JOptionPane.WARNING_MESSAGE);
                app.switchPage("HOME"); 
                return;
            }

            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                String name = DataStore.wishNames.get(index);
                double price = DataStore.wishPrices.get(index);
                double currentBalance = DataStore.currentBalance;

                String input = JOptionPane.showInputDialog(this, "คุณคาดว่าจะใช้งาน '" + name + "' ประมาณกี่ครั้ง?");
                if (input != null && !input.isEmpty()) {
                    try {
                        int times = Integer.parseInt(input);
                        double cpu = price / times;
                        
                        String advice = (price > currentBalance) ? "เงินไม่พอซื้อครับ!" : 
                                       (cpu <= 50) ? "คุ้มมาก! เฉลี่ยครั้งละ ฿" + String.format("%.2f", cpu) : "แอบแพงนะ... เฉลี่ยครั้งละ ฿" + String.format("%.2f", cpu);

                        int finalChoice = JOptionPane.showConfirmDialog(this, 
                            "Item: " + name + "\n" + advice + "\nยืนยันจะซื้อหรือไม่?", 
                            "วิเคราะห์ความคุ้มค่า", JOptionPane.YES_NO_OPTION);
                        
                        if (finalChoice == JOptionPane.YES_OPTION) {
                            if (currentBalance >= price) {
                                DataStore.currentBalance -= price;
                                DataStore.history.add("- [Wishlist] " + name + " (฿" + String.format("%.2f", price) + ")");
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
                JOptionPane.showMessageDialog(this, "เลือกรายการจากลิสต์ก่อนนะจ๊ะ");
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
                JOptionPane.showMessageDialog(this, "เลือกรายการที่จะลบก่อนจ้า");
            }
        });

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnAnalyze);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updateList(); 
    }

    public void updateList() {
        listModel.clear();
        for (int i = 0; i < DataStore.wishNames.size(); i++) {
            listModel.addElement((i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + String.format("%.2f", DataStore.wishPrices.get(i)));
        }
    }
}