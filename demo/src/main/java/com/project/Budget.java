package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Budget extends JPanel {
    private JTextField nameField, priceField, usageField;
    private JButton btnDecide, btnBack;

    public Budget(App app) {
        setLayout(new BorderLayout());

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
        gbc.fill = GridBagConstraints.HORIZONTAL; // ให้ขยายเต็มแนวนอน

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
        btnDecide = new JButton("Analyze Purchase");
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
        try {
            String name = nameField.getText().trim();
            String priceStr = priceField.getText().trim();
            String usageStr = usageField.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || usageStr.isEmpty()) {
                throw new Exception("กรุณากรอกข้อมูลให้ครบถ้วน");
            }

            double price = Double.parseDouble(priceStr);
            int usage = Integer.parseInt(usageStr);
            double currentBalance = DataStore.currentBalance;

            if (usage <= 0) throw new Exception("จำนวนครั้งต้องมากกว่า 0");

            double cpu = price / usage; // Cost Per Use
            
            JLabel labelVerdict = new JLabel();
            labelVerdict.setFont(new Font("Tahoma", Font.BOLD, 26));
            labelVerdict.setHorizontalAlignment(SwingConstants.CENTER);
            
            String advice = "";
            
            // --- Logic การตัดสินใจ (ความคุ้มค่าเทียบกับงบที่มี) ---
            if (price > currentBalance) {
                labelVerdict.setText("NO BUDGET!");
                labelVerdict.setForeground(Theme.Price);
                advice = "เงินไม่พอซื้อครับ! เกินงบไป " + String.format("%.2f", price - currentBalance) + " บาท";
            } 
            else if (usage == 1 && price > (currentBalance * 0.05)) {
                labelVerdict.setText("NOT WORTH IT!");
                labelVerdict.setForeground(Theme.Price);
                advice = "ไม่คุ้มเลย! ใช้ครั้งเดียวแต่จ่ายหนักถึง " + String.format("%.1f%%", (price/currentBalance)*100) + " ของเงินที่มี";
            }
            else if (cpu > (currentBalance * 0.1)) {
                labelVerdict.setText("THINK AGAIN!");
                labelVerdict.setForeground(Color.ORANGE);
                advice = "ราคาต่อการใช้ 1 ครั้งแอบสูงนะ (฿" + String.format("%.2f", cpu) + ") ลองคิดดูอีกทีไหม?";
            }
            else {
                labelVerdict.setText("WORTH IT!");
                labelVerdict.setForeground(Theme.INCOME);
                advice = "จัดเลย! เฉลี่ยจ่ายเพียงครั้งละ " + String.format("%.2f", cpu) + " บาทเท่านั้น";
            }

            String message = "Item: " + name.toUpperCase() + "\n" + advice + "\n\nคุณตัดสินใจจะซื้อรายการนี้หรือไม่?";
            
            UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
            int choice = JOptionPane.showConfirmDialog(app, new Object[]{message, labelVerdict}, "Analysis Result", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                DataStore.currentBalance -= price;
                DataStore.history.add("- " + name + " (฿" + String.format("%.2f", price) + ")");
                JOptionPane.showMessageDialog(app, "บันทึกสำเร็จ! เหลือเงิน: ฿" + String.format("%.2f", DataStore.currentBalance));

                // ล้างข้อมูลหลังบันทึก
                nameField.setText("");
                priceField.setText("");
                usageField.setText("");
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกราคาและจำนวนครั้งเป็นตัวเลข", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}