package com.project;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image bg;

    public BackgroundPanel() {
        bg = new ImageIcon(
                getClass().getResource(
                        "/com/project/Image/BG.jpg"
                )
        ).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(bg, 0, 0,
                getWidth(), getHeight(), this);

       
        
    }
}