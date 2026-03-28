package com.project;

import javax.swing.*;
import java.awt.*;

public class Wishlist extends JPanel {
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> wishlistDisplay = new JList<>(listModel);
    
    // ตั้งค่าฟอนต์มาตรฐานไว้ใช้ในหน้านี้
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
    private Font thaiFontBold = new Font("Tahoma", Font.BOLD, 14);

    public Wishlist(App app) {
        setLayout(new BorderLayout());

        // --- ส่วนหัว (Header) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(thaiFont); // เซตฟอนต์ปุ่ม Back
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
        JButton btnDelete = new JButton("ลบรายการที่เลือก");
        JButton btnBuy = new JButton("ซื้อเลย (หักเงินจริง)");

        // เซตฟอนต์ให้ปุ่มด้านล่างทั้งหมด
        btnAdd.setFont(thaiFont);
        btnDelete.setFont(thaiFont);
        btnBuy.setFont(thaiFont);

        // Logic: เพิ่มรายการใหม่
        btnAdd.addActionListener(e -> {
            // เซตฟอนต์ให้ช่อง Input ผ่าน UIManager ชั่วคราว
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

        // Logic: ซื้อสินค้า
        btnBuy.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                double price = DataStore.wishPrices.get(index);
                String name = DataStore.wishNames.get(index);

                if (DataStore.currentBalance >= price) {
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
        });

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnBuy);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateList() {
        listModel.clear();
        if (DataStore.wishNames.isEmpty()) {
        } else {
            for (int i = 0; i < DataStore.wishNames.size(); i++) {
                listModel.addElement((i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + DataStore.wishPrices.get(i));
            }
        }
    }
}