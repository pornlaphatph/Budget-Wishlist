package com.project;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Stats extends JPanel {
    private JButton btnBack;
    private Map<String, Color> categoryColors;

    public Stats(App app) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        categoryColors = new HashMap<>();
        categoryColors.put("ทั่วไป", new Color(118, 238, 198));
        categoryColors.put("อาหาร", new Color(255, 99, 71));
        categoryColors.put("ไอที/เกม", new Color(156, 156, 156));
        categoryColors.put("เสื้อผ้า", new Color(224, 102, 255));
        categoryColors.put("สุขภาพ/ของใช้", new Color(144, 238, 144));
        categoryColors.put("เครื่องสำอาง", new Color(255, 193, 193));
        categoryColors.put("อื่นๆ", new Color(238, 203, 173));   

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Theme.PRIMARY);
        btnBack = new JButton("← Back");
        btnBack.addActionListener(e -> app.switchPage("HOME"));
        header.add(btnBack);
        
        JLabel title = new JLabel("สัดส่วนค่าใช้จ่ายรายหมวด");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH );

        // ส่วนวาดกราฟ
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPieChart(g);
            }
        };
        chartPanel.setOpaque(false);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void drawPieChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (DataStore.categoryTotals.isEmpty()) {
            g2.setFont(new Font("Tahoma", Font.PLAIN, 18));
            g2.drawString("No purchase yet.", 150, 150);
            return;
        }

        double totalSpending = 0;
        for (double val : DataStore.categoryTotals.values()) {
            totalSpending += val;
        }

        int x = 100, y = 50, width = 300, height = 300;
        int startAngle = 0;

        int labelY = 70; // ตำแหน่งเริ่มเขียนคำอธิบายสี (Legend)
        
        for (Map.Entry<String, Double> entry : DataStore.categoryTotals.entrySet()) {
            String cat = entry.getKey();
            double value = entry.getValue();
            
            int arcAngle = (int) Math.round((value / totalSpending) * 360);
            
            
            g2.setColor(categoryColors.getOrDefault(cat, Color.GRAY));
            
            
            g2.fillArc(x, y, width, height, startAngle, arcAngle);
            
            g2.fillRect(450, labelY, 20, 20);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Tahoma", Font.PLAIN, 14));
            String info = String.format("%s: ฿%.2f (%.1f%%)", cat, value, (value/totalSpending)*100);
            g2.drawString(info, 480, labelY + 15);
            
            startAngle += arcAngle;
            labelY += 30;
        }

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, width, height);
        
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Tahoma", Font.BOLD, 16));
        g2.drawString("Total: ฿" + String.format("%.2f", totalSpending), 100, 400);
    }
}