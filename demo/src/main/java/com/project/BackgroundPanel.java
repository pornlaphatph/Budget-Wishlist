package com.project;
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image bg;
    public BackgroundPanel() {
        setLayout(new GridBagLayout());
        try {
            bg = new ImageIcon(getClass().getResource("/com/project/Image/BG.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("Image not found, using default color.");
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}