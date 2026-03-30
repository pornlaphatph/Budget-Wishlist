package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Budget extends JPanel {
    private JTextField nameField, priceField, usageField;
    private JComboBox<String> categoryBox; 
    private JButton btnDecide, btnBack;
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);
    private String[] categories = {"ทั่วไป", "อาหาร", "ไอที/เกม", "เสื้อผ้า", "สุขภาพ/ของใช้", "เครื่องสำอาง", "อื่นๆ"};

    public Budget(App app) {
        setLayout(new BorderLayout());

        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);
        UIManager.put("TextField.font", thaiFont);

        // ===== Header =====
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 70));

        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> app.switchPage("HOME"));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel title = new JLabel("ซื้อดีมั้ยน้า?");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));

        header.add(btnBack);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== Main Content Panel =====
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel card = new JPanel(new GridLayout(4, 2, 10, 15));
        card.setPreferredSize(new Dimension(400, 250)); 
        card.setBackground(Color.WHITE); 
        card.setBorder(BorderFactory.createTitledBorder(null, "Purchase Details", 0, 0, new Font("Tahoma", Font.BOLD, 12)));

        card.add(new JLabel(" Item Name:"));
        nameField = new JTextField();
        card.add(nameField);

        card.add(new JLabel(" Category:"));
        categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(thaiFont);
        categoryBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(categoryBox);

        card.add(new JLabel(" Price (฿):"));
        priceField = new JTextField();
        card.add(priceField);

        card.add(new JLabel(" Usage (times/year):"));
        usageField = new JTextField();
        usageField.addActionListener(e -> processAnalysis(app));
        card.add(usageField);

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(card, gbc);

        btnDecide = new JButton("คำนวณเลย!");
        btnDecide.setPreferredSize(new Dimension(380, 50));
        btnDecide.setBackground(Theme.PRIMARY);
        btnDecide.setForeground(Color.WHITE);
        btnDecide.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnDecide.addActionListener(e -> processAnalysis(app));
        btnDecide.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 1;
        mainPanel.add(btnDecide, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void processAnalysis(App app) {
        if(DataStore.currentBalance <= 0) {
            JOptionPane.showMessageDialog(this, "คุณยังไม่ได้ตั้ง Budget เลย!", "WARNING!", JOptionPane.WARNING_MESSAGE);
            app.switchPage("HOME");
            return;
        }

        try {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String usageStr = usageField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();

            if (name.isEmpty() || priceStr.isEmpty() || usageStr.isEmpty()) {
                throw new Exception("กรุณากรอกข้อมูลให้ครบถ้วนก่อนนะจ๊ะ");
            }

            double price = Double.parseDouble(priceStr);
            int usage = Integer.parseInt(usageStr);
            double currentBalance = DataStore.currentBalance;

            if (price <= 0 || usage <= 0) throw new Exception("ราคาและจำนวนครั้งต้องมากกว่า 0");

            double cpu = price / usage; 
            JLabel labelVerdict = new JLabel();
            labelVerdict.setFont(new Font("Tahoma", Font.BOLD, 26));
            labelVerdict.setHorizontalAlignment(SwingConstants.CENTER);
            
            String advice = "";
            
            // 1. Logic วิเคราะห์พื้นฐาน
            if (price > currentBalance) {
                labelVerdict.setText("NO BUDGET!");
                labelVerdict.setForeground(Theme.Price);
                advice = "เงินไม่พอครับ! ขาดอีก " + String.format("%.2f", price - currentBalance) + " บาท";
            } else if (usage == 1 && price > (currentBalance * 0.05)) {
                labelVerdict.setText("NOT WORTH IT!");
                labelVerdict.setForeground(Theme.Price);
                advice = "ใช้ครั้งเดียวแต่จ่ายถึง " + String.format("%.1f%%", (price/currentBalance)*100) + " ของงบที่มี ไม่คุ้มเลย";
            } else if (cpu > (currentBalance * 0.1) || price > (currentBalance * 0.4)) {
                labelVerdict.setText("THINK AGAIN!");
                labelVerdict.setForeground(Color.ORANGE);
                advice = "ราคาต่อครั้งคือ ฿" + String.format("%.2f", cpu) + " คิดดูอีกทีนะ";
            } else {
                labelVerdict.setText("WORTH IT!");
                labelVerdict.setForeground(Theme.INCOME);
                advice = "คุ้มมาก! จ่ายเฉลี่ยครั้งละ ฿" + String.format("%.2f", cpu) + " เท่านั้น";
            }

            //[Smart Logic] วิเคราะห์ตามหมวดหมู่
            int categoryCount = 0;
            for (String cat : DataStore.historyCategories) {
                if (cat.equals(category)) categoryCount++;
            }

            String smartAdvice = "";
            if (category.equals("สุขภาพ/ของใช้")) {
                smartAdvice = "\n\n[Health] เป็นการลงทุนที่คุ้มค่าเพื่อตัวเองครับ";
            } 
            else if (category.equals("ไอที/เกม") && categoryCount >= 3) {
                smartAdvice = "\n\n[Warning] เดือนนี้เปย์ไอทีไป " + categoryCount + " ชิ้นแล้วนะ พักก่อน!";
                labelVerdict.setText("HOLD ON!");
                labelVerdict.setForeground(Color.RED);
            }
            else if (category.equals("เครื่องสำอาง") && price > 1000) {
                smartAdvice = "\n\n[Beauty] สวยแบบพอดีๆ ในงบที่เหมาะสมจะดีกว่านะจ๊ะ";
            }
            else if (category.equals("อาหาร") && price > 500) {
                smartAdvice = "\n\n[Foodie] มื้อนี้หรูจัง อย่าลืมประหยัดมื้อถัดไปล่ะ";
            }

            advice += smartAdvice;

            // 3. แสดงผลหน้าต่างตัดสินใจ
            String message = "Item: " + name.toUpperCase() + " [" + category + "]\n" + advice + "\n\nตัดสินใจอย่างไร?";
            Object[] options = {"ซื้อเลย", "ใส่ Wishlist", "ยกเลิก"};
            
            int choice = JOptionPane.showOptionDialog(app, new Object[]{message, labelVerdict}, 
                    "Analysis Result", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);

            if (choice == JOptionPane.YES_OPTION) {
                if (currentBalance >= price) {
                    DataStore.currentBalance -= price;
                    DataStore.history.add("- [" + category + "] " + name + " (฿" + String.format("%.2f", price) + ")");
                    DataStore.historyCategories.add(category);
                    JOptionPane.showMessageDialog(app, "บันทึกเรียบร้อย เหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));
                    DataStore.categoryTotals.put(category, DataStore.categoryTotals.getOrDefault(category, 0.0) + price);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(app, "เงินไม่พอจ้า!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } 
            else if (choice == JOptionPane.NO_OPTION) {
                DataStore.wishNames.add(name);
                DataStore.wishPrices.add(price);
                DataStore.wishCategories.add(category);

                String savingAdvice = "";
                if (currentBalance < price) {
                    double gap = price - currentBalance;
                    savingAdvice = "\n\nต้องเก็บเงินเพิ่มอีก ฿" + String.format("%.2f", gap) + " นะ";
                } else {
                    savingAdvice = "\n\nงบพอซื้อ แต่เก็บไว้พิจารณาก็ดีครับ";
                }

                Integer[] stars = {1, 2, 3, 4, 5};
                Integer priority = (Integer) JOptionPane.showInputDialog(app, "ความจำเป็นระดับไหน?" + savingAdvice, "Set Priority", JOptionPane.QUESTION_MESSAGE, null, stars, 3);
                DataStore.wishPriorities.add(priority == null ? 3 : priority);

                JOptionPane.showMessageDialog(app, "เพิ่มเข้า Wishlist แล้ว");
                clearFields();
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขให้ถูกต้อง", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        usageField.setText("");
        categoryBox.setSelectedIndex(0);
        nameField.requestFocus();
    }
}