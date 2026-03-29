package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Budget extends JPanel {
    private JTextField nameField, priceField, usageField;
    private JButton btnDecide, btnBack;
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

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

        // ===== Card (Input Fields) =====
        JPanel card = new JPanel(new GridLayout(3, 2, 10, 15));
        card.setPreferredSize(new Dimension(400, 200)); 
        card.setBackground(Color.WHITE); 
        card.setBorder(BorderFactory.createTitledBorder(null, "Purchase Details", 0, 0, new Font("Tahoma", Font.BOLD, 12)));

        card.add(new JLabel(" Item Name:"));
        nameField = new JTextField();
        nameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        card.add(nameField);

        card.add(new JLabel(" Price (฿):"));
        priceField = new JTextField();
        priceField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        card.add(priceField);

        card.add(new JLabel(" Usage (times/year):"));
        usageField = new JTextField();
        usageField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        card.add(usageField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(card, gbc);

        // ===== Button Analyze =====
        btnDecide = new JButton("คำนวณเลย!");
        btnDecide.setPreferredSize(new Dimension(380, 50));
        btnDecide.setBackground(Theme.PRIMARY);
        btnDecide.setForeground(Color.WHITE);
        btnDecide.setFont(new Font("Tahoma", Font.BOLD, 16));

        gbc.gridy = 1;
        mainPanel.add(btnDecide, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // ===== Logic การวิเคราะห์ =====
        btnDecide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAnalysis(app);
            }
        });
    }

    private void processAnalysis(App app) {

        if(DataStore.currentBalance <= 0) {
            JOptionPane.showMessageDialog(this, 
                "คุณยังไม่ได้ตั้ง Budget เลย! กลับไปตั้งงบก่อนนะจ๊ะ", "WARNING!", JOptionPane.WARNING_MESSAGE);
            
            app.switchPage("HOME");
            return;
        }
        

        try {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String usageStr = usageField.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || usageStr.isEmpty()) {
                throw new Exception("กรุณากรอกข้อมูลให้ครบถ้วนก่อนนะจ๊ะ");
            }

            double price = Double.parseDouble(priceStr);
            int usage = Integer.parseInt(usageStr);
            double currentBalance = DataStore.currentBalance;

            if (usage <= 0) throw new Exception("จำนวนครั้งต้องมากกว่า 0 นะ");

            double cpu = price / usage; 
            
            JLabel labelVerdict = new JLabel();
            labelVerdict.setFont(new Font("Tahoma", Font.BOLD, 26));
            labelVerdict.setHorizontalAlignment(SwingConstants.CENTER);
            
            String advice = "";
            
            // --- Logic ดึงสติ---
            if (price > currentBalance) {
                labelVerdict.setText("NO BUDGET!");
                labelVerdict.setForeground(Theme.Price);
                advice = "เงินไม่พอซื้อครับ! เกินงบไป " + String.format("%.2f", price - currentBalance) + " บาท";
            } 
            else if (usage == 1 && price > (currentBalance * 0.05)) {
                labelVerdict.setText("NOT WORTH IT!");
                labelVerdict.setForeground(Theme.Price);
                advice = "ไม่คุ้มเลย! ใช้ครั้งเดียวแต่จ่ายหนักถึง " + String.format("%.1f%%", (price/currentBalance)*100) + " ของงบที่คุณมีนะ";
            }
            else if (cpu > (currentBalance * 0.1) || price > (currentBalance * 0.4) || (price > 1000 && usage < 12)) {
                labelVerdict.setText("THINK AGAIN!");
                labelVerdict.setForeground(Color.ORANGE);
                
                if (price > (currentBalance * 0.4)) {
                    advice = "ระวัง! ของชิ้นนี้ราคาสูงถึง " + String.format("%.0f%%", (price/currentBalance)*100) + " ของงบที่คุณมีนะ";
                } else if (price > 1000 && usage < 12) {
                    advice = "ของราคาหลักพันแต่ใช้เฉลี่ยเดือนละไม่ถึงครั้ง... ลองคิดดูอีกทีไหม?";
                } else {
                    advice = "ราคาต่อการใช้ 1 ครั้งแอบสูงนะ (฿" + String.format("%.2f", cpu) + ")";
                }
            }
            else {
                labelVerdict.setText("WORTH IT!");
                labelVerdict.setForeground(Theme.INCOME);
                advice = "ของมันต้องมีแล้วมั้ย! เฉลี่ยจ่ายเพียงครั้งละ " + String.format("%.2f", cpu) + " บาทเองนะ ถือว่าคุ้มมาก!";
            }

            String message = "Item: " + name.toUpperCase() + "\n" + advice + "\n\nคุณจะตัดสินใจอย่างไร?";
            
            
            Object[] options = {"ซื้อเลย", "ใส่ Wishlist ไว้ก่อน", "ยกเลิก"};
            UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
            
            int choice = JOptionPane.showOptionDialog(app, 
                    new Object[]{message, labelVerdict}, 
                    "Analysis Result",
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE, 
                    null, options, options[2]);

            
            if (choice == JOptionPane.YES_OPTION) {
                if (DataStore.currentBalance >= price) {
                    DataStore.currentBalance -= price;
                    DataStore.history.add("- " + name + " (฿" + String.format("%.2f", price) + ")");
                    JOptionPane.showMessageDialog(app, "ลงประวัติเรียบร้อย คุณเหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));
                } else {
                    JOptionPane.showMessageDialog(app, "เงินไม่พอจ้า! ลองเพิ่มเข้า Wishlist แทนนะ", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } 
            else if (choice == JOptionPane.NO_OPTION) {  // ส่วนของ wishlist

                DataStore.wishNames.add(name);
                DataStore.wishPrices.add(price);
                JOptionPane.showMessageDialog(app, "เพิ่ม '" + name + "' เข้าใน Wishlist เรียบร้อยแล้วจ้า");
            }
            clearFields();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกราคาและจำนวนครั้งเป็นตัวเลข", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        usageField.setText("");
        nameField.requestFocus();
    }
}