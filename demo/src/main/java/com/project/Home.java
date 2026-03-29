package com.project;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class Home extends JPanel {
    private JLabel lblBalance;
    private JTextArea txtHistory;
    private JTextField budgetInput;
    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

    public Home(App app) {
        setLayout(new BorderLayout());
        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);
        UIManager.put("TextField.font", thaiFont);
        
        // ===== Header (ส่วนแสดงงบประมาณคงเหลือ) =====
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 130));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Remaining Budget");
        title.setFont(new Font("Tahoma", Font.PLAIN, 14)); 
        title.setForeground(new Color(200, 220, 240));
        
        lblBalance = new JLabel("฿ 0.00");
        lblBalance.setFont(new Font("Tahoma", Font.BOLD, 36)); 
        lblBalance.setForeground(Color.WHITE);
        header.add(title);
        header.add(lblBalance);
        add(header, BorderLayout.NORTH);

        // ===== Center Content (ส่วนปุ่มควบคุม) =====
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBudget = new JLabel("Budget :");
        lblBudget.setFont(new Font("Tahoma", Font.PLAIN, 14)); 

        budgetInput = new JTextField(10);
        budgetInput.setFont(new Font("Tahoma", Font.PLAIN, 14)); 

        JButton btnSet = new JButton("Set Budget");
        btnSet.setFont(new Font("Tahoma", Font.BOLD, 12)); 

        // --- Logic สำหรับ Set Budget ---
        ActionListener setBudgetAction = e -> performSetBudget();
        btnSet.addActionListener(setBudgetAction);
        budgetInput.addActionListener(setBudgetAction);

        JButton btnGo = new JButton("คุ้มป่าวน้าา? ");
        btnGo.setFont(new Font("Tahoma", Font.BOLD, 14)); 
        btnGo.setPreferredSize(new Dimension(0, 40));
        btnGo.addActionListener(e -> app.switchPage("BUDGET"));

        JButton btnWish = new JButton("รายการที่เล็งไว้ ");
        btnWish.setFont(new Font("Tahoma", Font.BOLD, 14)); 
        btnWish.setPreferredSize(new Dimension(0, 40));
        btnWish.addActionListener(e -> app.switchPage("WISHLIST"));

        JButton btnStats = new JButton("ดูสถิติการใช้จ่าย");
        btnStats.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnStats.setPreferredSize(new Dimension(0, 40));
        btnStats.addActionListener(e -> app.switchPage("STATS"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        center.add(lblBudget, gbc);
        gbc.gridx = 1; center.add(budgetInput, gbc);
        gbc.gridx = 2; center.add(btnSet, gbc);
        
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 3;
        center.add(btnGo, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 3;
        center.add(btnWish, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0; 
        gbc.gridwidth = 3;
        center.add(btnStats, gbc);

        add(center, BorderLayout.CENTER);

        // ===== South Content (ประวัติการซื้อ) =====
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        southPanel.setPreferredSize(new Dimension(0, 200));

        txtHistory = new JTextArea();
        txtHistory.setEditable(false);
        txtHistory.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        txtHistory.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(txtHistory);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("ประวัติการซื้อ :");
        titledBorder.setTitleFont(new Font("Tahoma", Font.BOLD, 13)); 
        scrollPane.setBorder(titledBorder);
        
        southPanel.add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        SwingUtilities.invokeLater(() -> budgetInput.requestFocusInWindow());
    }

    private void performSetBudget() {
        try {
            String text = budgetInput.getText().trim();
            if (text.isEmpty()) {
                throw new Exception("กรุณากรอกจำนวนเงินด้วยนะจ๊ะ");
            }

            double amount = Double.parseDouble(text);
            if (amount < 0) {
                throw new Exception("งบประมาณต้องไม่ติดลบนะ");
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "การตั้งงบใหม่จะล้างประวัติการซื้อและสถิติเดิม คุณแน่ใจนะ?", 
                "Confirm Set Budget", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                DataStore.maxBudget = amount;
                DataStore.currentBalance = amount;
                DataStore.history.clear(); 
                DataStore.historyCategories.clear(); 
                updateDisplay();
                budgetInput.setText("");
                JOptionPane.showMessageDialog(this, "ตั้งงบประมาณเรียบร้อยแล้ว!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขที่ถูกต้อง", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateDisplay() {
        lblBalance.setText(String.format("฿ %.2f", DataStore.currentBalance));
        
    
        if (DataStore.currentBalance < 0) {
            lblBalance.setForeground(new Color(255, 100, 100)); 
        } else {
            lblBalance.setForeground(Color.WHITE);
        }

        txtHistory.setText(""); 
        if (DataStore.history.isEmpty()) {
            txtHistory.setText("No purchase yet.");
        } else {
            for (String item : DataStore.history) {
                txtHistory.append(item + "\n");
            }
        }
    }
}