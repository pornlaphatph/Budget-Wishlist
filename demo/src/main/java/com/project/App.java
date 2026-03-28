package com.project;

import javax.swing.*;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
       
    SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Budget app = new Budget();
                app.setTitle("YET?");
                app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                app.setSize(1000, 700);
                app.setLocationRelativeTo(null);
                app.setVisible(true);
            }
        });     
    }
}