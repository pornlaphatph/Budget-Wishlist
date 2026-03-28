package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Budget extends JPanel {

    public Budget(App app) {
        setLayout(new BorderLayout());

        // ===== Header =====
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 70));

        JButton btnBack = new JButton("← Back");
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> app.switchPage("HOME")); 
        
        JLabel title = new JLabel("ซื้อดีมั้ยน้า?");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));

        header.add(btnBack);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== Main Content Panel with Background =====
        BackgroundPanel mainPanel = new BackgroundPanel(); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // ===== Card =====
        JPanel card = new JPanel(new GridLayout(3, 2, 10, 15));
        card.setPreferredSize(new Dimension(400, 200)); 
        card.setBackground(new Color(255, 255, 255, 220)); 
        card.setBorder(BorderFactory.createTitledBorder("Purchase Details"));

        card.add(new JLabel(" Item Name:"));
        JTextField nameField = new JTextField();
        card.add(nameField);

        card.add(new JLabel(" Price (฿):"));
        JTextField priceField = new JTextField();
        card.add(priceField);

        card.add(new JLabel(" Usage (times/year):"));
        JTextField usageField = new JTextField();
        card.add(usageField);

        gbc.gridy = 0;
        mainPanel.add(card, gbc);

        // ===== Button Analyze =====
        JButton btnDecide = new JButton("Analyze Purchase 🔍");
        btnDecide.setPreferredSize(new Dimension(380, 50));
        btnDecide.setBackground(Theme.PRIMARY);
        btnDecide.setForeground(Color.WHITE);
        btnDecide.setFont(new Font("SansSerif", Font.BOLD, 16));

        gbc.gridy = 1;
        mainPanel.add(btnDecide, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // ===== Logic: วิเคราะห์ -> ถามยืนยัน -> หักงบ -> บันทึกประวัติ =====
        btnDecide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    String priceStr = priceField.getText().trim();
                    String usageStr = usageField.getText().trim();

                    if (name.isEmpty() || priceStr.isEmpty() || usageStr.isEmpty()) {
                        throw new Exception("กรุณากรอกข้อมูลให้ครบถ้วน");
                    }

                    double price = Double.parseDouble(priceStr);
                    int usage = Integer.parseInt(usageStr);

                    if (usage <= 0) throw new Exception("จำนวนครั้งต้องมากกว่า 0");

                    double cpu = price / usage; // Cost Per Use
                    
                    JLabel labelVerdict = new JLabel();
                    labelVerdict.setFont(new Font("SansSerif", Font.BOLD, 24));
                    labelVerdict.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    String advice = "";
                    if (cpu <= 100) {
                        labelVerdict.setText("✅ WORTH IT!");
                        labelVerdict.setForeground(Theme.INCOME);
                        advice = "คุ้มค่ามาก! ตกครั้งละ " + String.format("%.2f", cpu) + " บาท";
                    } else if (cpu <= 500) {
                        labelVerdict.setText("⏳ HOLD ON!");
                        labelVerdict.setForeground(Theme.PRIMARY);
                        advice = "ราคาแอบแรงนะ ลองคิดดูอีกทีไหม?";
                    } else {
                        labelVerdict.setText("❌ TOO EXPENSIVE!");
                        labelVerdict.setForeground(Theme.Price);
                        advice = "แพงเกินไป ไม่ค่อยคุ้มเลยครับ";
                    }

                    String message = "Item: " + name.toUpperCase() + "\n" + advice + "\n\nคุณตัดสินใจจะซื้อรายการนี้หรือไม่?";
                    Object[] params = { message, labelVerdict };
                    
                    int choice = JOptionPane.showConfirmDialog(app, params, "Analysis Result", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // --- ถ้าผู้ใช้กดปุ่ม YES (ตกลงซื้อ) ---
                    if (choice == JOptionPane.YES_OPTION) {
                        //หักเงินในงบประมาณหลัก
                        DataStore.currentBalance -= price;

                        //บันทึกลงในรายการประวัติ (History)
                        String record = String.format("- %s (฿%.2f)", name, price);
                        DataStore.history.add(record);

                        //แจ้งเตือนความสำเร็จ
                        JOptionPane.showMessageDialog(app, "บันทึกการซื้อสำเร็จ!\nยอดเงินคงเหลือ: ฿" + String.format("%.2f", DataStore.currentBalance));

                        //ล้างค่าในช่องกรอกข้อมูล
                        nameField.setText("");
                        priceField.setText("");
                        usageField.setText("");
                    }

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(app, "กรุณากรอกตัวเลขในช่องราคาและจำนวนครั้ง", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(app, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}