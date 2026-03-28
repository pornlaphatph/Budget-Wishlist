package com.project;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Home extends JPanel {
    private JLabel lblBalance;
    private JTextArea txtHistory;

    public Home(App app) {
        setLayout(new BorderLayout());
        
        // ===== Header =====
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Theme.PRIMARY);
        header.setPreferredSize(new Dimension(0, 150));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Remaining Budget");
        title.setFont(new Font("Tahoma", Font.PLAIN, 14)); // แก้ฟอนต์
        title.setForeground(new Color(200, 220, 240));
        
        lblBalance = new JLabel("฿ 0.00");
        lblBalance.setFont(new Font("Tahoma", Font.BOLD, 36)); // แก้ฟอนต์
        lblBalance.setForeground(Color.WHITE);
        header.add(title);
        header.add(lblBalance);
        add(header, BorderLayout.NORTH);

        // ===== Center Content =====
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBudget = new JLabel("Budget :");
        lblBudget.setFont(new Font("Tahoma", Font.PLAIN, 14)); 

        JTextField budgetInput = new JTextField(10);
        budgetInput.setFont(new Font("Tahoma", Font.PLAIN, 14)); 

        JButton btnSet = new JButton("Set Budget");
        btnSet.setFont(new Font("Tahoma", Font.BOLD, 12)); 
        btnSet.addActionListener(e -> {
            try {
                DataStore.maxBudget = Double.parseDouble(budgetInput.getText());
                DataStore.currentBalance = DataStore.maxBudget;
                DataStore.history.clear(); 
                updateDisplay();
                budgetInput.setText(""); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "กรอกตัวเลขที่ถูกต้อง");
            }
        });

        JButton btnGo = new JButton("คุ้มป่าวน้าา?");
        btnGo.setFont(new Font("Tahoma", Font.BOLD, 14)); 
        btnGo.setPreferredSize(new Dimension(0, 40));
        btnGo.addActionListener(e -> app.switchPage("BUDGET"));

        gbc.gridx = 0; gbc.gridy = 0; center.add(lblBudget, gbc);
        gbc.gridx = 1; center.add(budgetInput, gbc);
        gbc.gridx = 2; center.add(btnSet, gbc);
        
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 3;
        center.add(btnGo, gbc);

        add(center, BorderLayout.CENTER);

        // ===== South Content =====
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        southPanel.setPreferredSize(new Dimension(0, 200));

        txtHistory = new JTextArea();
        txtHistory.setEditable(false);
        txtHistory.setFont(new Font("Tahoma", Font.PLAIN, 13)); 
        txtHistory.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(txtHistory);
        
        // แก้ฟอนต์ตรงหัวข้อประวัติ (TitledBorder)
        TitledBorder titledBorder = BorderFactory.createTitledBorder("ประวัติการซื้อ :");
        titledBorder.setTitleFont(new Font("Tahoma", Font.BOLD, 13)); 
        scrollPane.setBorder(titledBorder);
        
        southPanel.add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void updateDisplay() {
        lblBalance.setText(String.format("฿ %.2f", DataStore.currentBalance));
        
        if (DataStore.currentBalance < 0) {
            lblBalance.setForeground(Color.YELLOW);
        } else {
            lblBalance.setForeground(Color.WHITE);
        }

        txtHistory.setText(""); 
        if (DataStore.history.isEmpty()) {
            txtHistory.setText("No purchases yet.");
        } else {
            for (String item : DataStore.history) {
                txtHistory.append(item + "\n");
            }
        }
    }
}