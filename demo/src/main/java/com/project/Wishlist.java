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
        
        JLabel title = new JLabel("Wishlist");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        header.add(btnBack);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- Center ---
        wishlistDisplay.setFont(new Font("Tahoma", Font.PLAIN, 16));
        setupRenderer(); 
        
        JScrollPane scrollPane = new JScrollPane(wishlistDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel();
        JButton btnAdd = new JButton("เพิ่มรายการ");
        JButton btnAnalyze = new JButton("วิเคราะห์และซื้อ");
        JButton btnSort = new JButton("เรียงความจำเป็น");
        JButton btnDelete = new JButton("ลบรายการ");

        btnAdd.setFont(thaiFont);
        btnAnalyze.setFont(thaiFont);
        btnSort.setFont(thaiFont);
        btnDelete.setFont(thaiFont);

        // Logic: เพิ่มรายการ
        btnAdd.addActionListener(e -> {
            UIManager.put("OptionPane.messageFont", thaiFont);
            UIManager.put("TextField.font", thaiFont);
            String name = JOptionPane.showInputDialog(this, "ชื่อสินค้า:");
            if (name != null && !name.isEmpty()) {
                try {
                    String priceStr = JOptionPane.showInputDialog(this, "ราคา (บาท):");
                    if (priceStr != null) {
                        double price = Double.parseDouble(priceStr);
                        Integer[] stars = {1, 2, 3, 4, 5};
                        Integer priority = (Integer) JOptionPane.showInputDialog(this, 
                            "ความจำเป็นระดับไหน? (5 = จำเป็นมาก)", "Priority",
                            JOptionPane.QUESTION_MESSAGE, null, stars, 3);
                        
                        if (priority != null) {
                            DataStore.wishNames.add(name);
                            DataStore.wishPrices.add(price);
                            DataStore.wishPriorities.add(priority);
                            updateList();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขราคาให้ถูกต้องนะจ๊ะ");
                }
            }
        });

        // Logic: วิเคราะห์และซื้อ
        btnAnalyze.addActionListener(e -> {
            if (DataStore.currentBalance <= 0) {
                JOptionPane.showMessageDialog(this, "ตั้ง Budget ก่อนนะจ๊ะ", "WARNING!", JOptionPane.WARNING_MESSAGE);
                app.switchPage("HOME");
                return;
            }

            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                String name = DataStore.wishNames.get(index);
                double price = DataStore.wishPrices.get(index);
                int priority = DataStore.wishPriorities.get(index);

                String input = JOptionPane.showInputDialog(this, "'" + name + "'\nคาดว่าจะใช้งานกี่ครั้ง?");
                if (input != null && !input.isEmpty()) {
                    try {
                        int times = Integer.parseInt(input);
                        double cpu = price / times;
                        
                        String advice = (priority == 5 && cpu < 50) ? "ของมันต้องมี! คุ้มมากซื้อเถอะ" : 
                                       (price > DataStore.currentBalance) ? "เงินไม่พอซื้อนะจ๊ะ" : "ลองตัดสินใจดูอีกทีนะ";

                        int finalChoice = JOptionPane.showConfirmDialog(this, 
                            "Item: " + name + "\nคำแนะนำ: " + advice + "\nยืนยันจะซื้อหรือไม่?", 
                            "วิเคราะห์ความคุ้มค่า", JOptionPane.YES_NO_OPTION);
                        
                        if (finalChoice == JOptionPane.YES_OPTION) {
                            if (DataStore.currentBalance >= price) {
                                DataStore.currentBalance -= price;
                                DataStore.history.add("- [Wishlist] " + name + " (฿" + String.format("%.2f", price) + ")");
                                removeData(index);
                                updateList();
                                JOptionPane.showMessageDialog(this, "ซื้อสำเร็จ! เหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));
                            } else {
                                JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า! งบเหลือแค่ ฿" + DataStore.currentBalance);
                            }
                        }
                    } catch (Exception ex) { 
                        JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขจำนวนครั้งด้วยนะ");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "เลือกรายการจากลิสต์ก่อนนะจ๊ะ");
            }
        });

        // Logic: เรียงลำดับ (Bubble Sort)
        btnSort.addActionListener(e -> {
            int n = DataStore.wishNames.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (DataStore.wishPriorities.get(j) < DataStore.wishPriorities.get(j + 1)) {
                        // Swap
                        String tName = DataStore.wishNames.get(j);
                        DataStore.wishNames.set(j, DataStore.wishNames.get(j+1));
                        DataStore.wishNames.set(j+1, tName);

                        Double tPrice = DataStore.wishPrices.get(j);
                        DataStore.wishPrices.set(j, DataStore.wishPrices.get(j+1));
                        DataStore.wishPrices.set(j+1, tPrice);

                        Integer tPrio = DataStore.wishPriorities.get(j);
                        DataStore.wishPriorities.set(j, DataStore.wishPriorities.get(j+1));
                        DataStore.wishPriorities.set(j+1, tPrio);
                    }
                }
            }
            updateList();
        });

        btnDelete.addActionListener(e -> {
            int index = wishlistDisplay.getSelectedIndex();
            if (index != -1) {
                removeData(index);
                updateList();
            }
        });

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnAnalyze);
        bottomPanel.add(btnSort);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupRenderer() {
        wishlistDisplay.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (index >= 0 && index < DataStore.wishPriorities.size()) {
                    int stars = DataStore.wishPriorities.get(index);
                    if (stars == 5) label.setBackground(new Color(210, 255, 210));
                    else if (stars <= 2) label.setBackground(new Color(255, 230, 230)); 
                    else label.setBackground(Color.WHITE);
                }
                
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                }
                label.setOpaque(true);
                return label;
            }
        });
    }

    private void removeData(int index) {
        if (index >= 0 && index < DataStore.wishNames.size()) {
            DataStore.wishNames.remove(index);
            DataStore.wishPrices.remove(index);
            DataStore.wishPriorities.remove(index);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updateList(); 
    }

    public void updateList() {
        listModel.clear();
        for (int i = 0; i < DataStore.wishNames.size(); i++) {
            int stars = DataStore.wishPriorities.get(i);
            
            String starDisplay = "";
            for (int s = 0; s < 5; s++) {
                starDisplay += (s < stars) ? "★" : "☆";
            }

            String htmlContent = "<html>" +
                "<body style='font-family: Tahoma; font-size: 16pt;'>" +
                (i + 1) + ". " + DataStore.wishNames.get(i) + " - ฿" + String.format("%.2f", DataStore.wishPrices.get(i)) + 
                " <span style='font-family: \"Segoe UI Symbol\"; color: #FF9900;'>[" + starDisplay + "]</span>" +
                "</body></html>";
            
            listModel.addElement(htmlContent);
        }
    }
}