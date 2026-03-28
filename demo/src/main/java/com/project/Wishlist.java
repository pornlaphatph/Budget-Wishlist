package com.project;

import javax.swing.*;
import java.awt.*;

public class Wishlist extends JPanel {
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> wishlistDisplay = new JList<>(listModel);

    public Wishlist(App app) {
        setLayout(new BorderLayout());

        // --- ส่วนหัว (Header) ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        JButton btnBack = new JButton("← Back");
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
        JButton btnAdd = new JButton("เพิ่มรายการ ➕");
        JButton btnDelete = new JButton("ลบรายการที่เลือก ➖");
        JButton btnBuy = new JButton("ซื้อเลย (หักเงินจริง) ✅");

        // Logic: เพิ่มรายการใหม่
        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "ชื่อสินค้าที่อยากได้:");
            if (name != null && !name.isEmpty()) {
                try {
                    String priceStr = JOptionPane.showInputDialog(this, "ราคา (บาท):");
                    double price = Double.parseDouble(priceStr);
                    
                    // บันทึกลง DataStore
                    DataStore.wishNames.add(name);
                    DataStore.wishPrices.add(price);
                    updateList(); // เปลี่ยนชื่อเรียกที่นี่ด้วย
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "กรุณากรอกราคาเป็นตัวเลขจ้า");
                }
            }
        });

        // Logic: ลบรายการ
        btnDelete.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                DataStore.wishNames.remove(index);
                DataStore.wishPrices.remove(index);
                updateList(); // เปลี่ยนชื่อเรียกที่นี่ด้วย
            } else {
                JOptionPane.showMessageDialog(this, "เลือกรายการที่จะลบก่อนนะ");
            }
        });

        // Logic: ซื้อสินค้า (หักเงินจาก Home)
        btnBuy.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                double price = DataStore.wishPrices.get(index);
                String name = DataStore.wishNames.get(index);

                if (DataStore.currentBalance >= price) {
                    DataStore.currentBalance -= price;
                    DataStore.history.add("- [Wishlist] " + name + " (฿" + price + ")");
                    
                    // ซื้อแล้วลบออกจาก Wishlist
                    DataStore.wishNames.remove(index);
                    DataStore.wishPrices.remove(index);
                    updateList(); // เปลี่ยนชื่อเรียกที่นี่ด้วย
                    JOptionPane.showMessageDialog(this, "ซื้อสำเร็จ! เหลือเงิน: ฿" + DataStore.currentBalance);
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
        for (int i = 0; i < DataStore.wishNames.size(); i++) {
            listModel.addElement((i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + DataStore.wishPrices.get(i));
        }
    }
}