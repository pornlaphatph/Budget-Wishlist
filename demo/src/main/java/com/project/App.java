package com.project;

import javax.swing.*;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
       
    SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Budget budget = new Budget();
                budget.setTitle("YET?");
                budget.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                budget.setSize(1000, 700);
                budget.setLocationRelativeTo(null);
                budget.setVisible(true);
            }
        });     
    }
}