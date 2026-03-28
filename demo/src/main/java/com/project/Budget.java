package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Budget extends JFrame {

    public Budget() {
        // ตั้งค่าหน้าต่างหลัก
        setTitle("My Budget");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Header =====
        JPanel header = new JPanel();
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 70));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20)); // จัดตัวอักษรให้อยู่กึ่งกลางแนวตั้ง

        JLabel title = new JLabel("My Budget");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== Main Content Panel (ใช้ GridBagLayout เพื่อจัดกึ่งกลาง) =====
        JPanel mainPanel = new BackgroundPanel(); // ตรวจสอบว่ามีคลาส BackgroundPanel ในโปรเจกต์
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // ระยะห่างระหว่าง Component
        gbc.gridx = 0; // คอลัมน์ที่ 0

        // ===== Card =====
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(400, 180));
        card.setBackground(Color.WHITE); // ให้การ์ดเป็นสีขาวเด่นออกมา
        card.setBorder(BorderFactory.createTitledBorder("Purchase Details"));
        card.setLayout(new GridLayout(3, 2, 10, 15));

        card.add(new JLabel(" Item Name:"));
        JTextField nameField = new JTextField();
        card.add(nameField);

        card.add(new JLabel(" Price (฿):"));
        JTextField priceField = new JTextField();
        card.add(priceField);

        card.add(new JLabel(" Usage (times/year):"));
        JTextField usageField = new JTextField();
        card.add(usageField);

        gbc.gridy = 0; // แถวที่ 0
        mainPanel.add(card, gbc);

        // ===== Button =====
        JButton btnDecide = new JButton("Analyze Purchase 🔍");
        btnDecide.setPreferredSize(new Dimension(380, 50));
        btnDecide.setBackground(Theme.PRIMARY);
        btnDecide.setForeground(Color.WHITE);
        btnDecide.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDecide.setFocusPainted(false);
        btnDecide.setBorderPainted(false);

        gbc.gridy = 1; // แถวที่ 1 (ต่อลงมาจาก Card)
        mainPanel.add(btnDecide, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // ===== Logic =====
        btnDecide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    String priceText = priceField.getText().trim();
                    String usageText = usageField.getText().trim();

                    if (name.isEmpty() || priceText.isEmpty() || usageText.isEmpty()) {
                        throw new Exception("Please fill all fields");
                    }

                    double price = Double.parseDouble(priceText);
                    int usage = Integer.parseInt(usageText);

                    if (usage <= 0) throw new Exception("Usage must be more than 0");

                    double cpu = price / usage;
                    
                    // --- หัวใจสำคัญ: สร้าง Component แยกตามเงื่อนไข ---
                    JLabel labelVerdict = new JLabel();
                    labelVerdict.setFont(new Font("SansSerif", Font.BOLD, 22)); // ปรับขนาดตัวโตๆ ใน Pop-up
                    labelVerdict.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    String messageBody = "Item: " + name.toUpperCase() + 
                                       "\nCost Per Use: ฿" + String.format("%.2f", cpu) + "\n\n";

                    if (cpu <= 100) {
                        labelVerdict.setText("✅ WORTH IT!");
                        labelVerdict.setForeground(Theme.INCOME);
                    } else if (cpu <= 500) {
                        labelVerdict.setText("⏳ HOLD ON!");
                        labelVerdict.setForeground(Theme.PRIMARY);
                    } else {
                        labelVerdict.setText("❌ TOO EXPENSIVE!");
                        labelVerdict.setForeground(Theme.Price); // เช็คว่าใน Theme.java ชื่อ Price หรือ EXPENSE
                    }

                    Object[] messageParams = {
                        messageBody,
                        labelVerdict
                    };

                    // ใช้ 'Budget.this' เพื่ออ้างอิงถึง JFrame หลัก
                    JOptionPane.showMessageDialog(Budget.this, messageParams, "Decision Result", JOptionPane.PLAIN_MESSAGE);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(Budget.this, "Price and Usage must be numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Budget.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        setLocationRelativeTo(null); // เด้งกลางจอ
    }
}