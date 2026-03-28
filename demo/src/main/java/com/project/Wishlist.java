package com.project;

import javax.swing.*;
import java.awt.*;

public class Wishlist extends JPanel {
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> listDisplay = new JList<>(listModel);

    public Wishlist(App app) {
        setLayout(new BorderLayout());

        // Header ส่วนหัว
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        JButton btnBack = new JButton("← กลับ");
        btnBack.addActionListener(e -> app.switchPage("HOME"));
        JLabel title = new JLabel("รายการที่เล็งไว้ (Wishlist)");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        header.add(btnBack);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ส่วนแสดงรายการ
        listDisplay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(new JScrollPane(listDisplay), BorderLayout.CENTER);

        // ปุ่มคำสั่งด้านล่าง
        JPanel footer = new JPanel();
        JButton btnBuy = new JButton("ซื้อชิ้นนี้ ✅");
        JButton btnDelete = new JButton("ลบทิ้ง 🗑️");

        btnBuy.addActionListener(e -> {
            int index = listDisplay.getSelectedIndex();
            if (index != -1) {
                String name = DataStore.wishNames.get(index);
                double price = DataStore.wishPrices.get(index);

                if (DataStore.currentBalance >= price) {
                    DataStore.currentBalance -= price; // หักเงินจริง
                    DataStore.history.add("- [Wishlist] " + name + " (฿" + price + ")"); // เพิ่มในประวัติ
                    DataStore.wishNames.remove(index);
                    DataStore.wishPrices.remove(index);
                    updateList();
                    JOptionPane.showMessageDialog(this, "ซื้อสำเร็จแล้ว!");
                } else {
                    JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า!");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int index = listDisplay.getSelectedIndex();
            if (index != -1) {
                DataStore.wishNames.remove(index);
                DataStore.wishPrices.remove(index);
                updateList();
            }
        });

        footer.add(btnBuy);
        footer.add(btnDelete);
        add(footer, BorderLayout.SOUTH);
    }

    public void updateList() {
        listModel.clear();
        for (int i = 0; i < DataStore.wishNames.size(); i++) {
            listModel.addElement((i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + DataStore.wishPrices.get(i));
        }
    }
}