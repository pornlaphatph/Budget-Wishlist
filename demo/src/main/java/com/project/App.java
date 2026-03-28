package com.project;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);
    private Home homePanel;
    private Wishlist wishlistPanel; 

    public App() {
        setTitle("YET?");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        homePanel = new Home(this);
        LoginGUI loginPanel = new LoginGUI(this);
        Budget budgetPanel = new Budget(this);
        wishlistPanel = new Wishlist(this); 

        
        container.add(loginPanel, "LOGIN"); 
        container.add(homePanel, "HOME");
        container.add(budgetPanel, "BUDGET");
        container.add(wishlistPanel, "WISHLIST"); 

        add(container);

        
        cardLayout.show(container, "LOGIN");
    }

    
    public void switchPage(String name) {
        cardLayout.show(container, name);
        
        
        if (name.equals("HOME")) {
            homePanel.updateDisplay();
        }
        
        
        if (name.equals("WISHLIST")) {
            wishlistPanel.updateList();
        }
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 14));
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}