package main.java.com.project;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    JPanel mainPanel;
    JTextField text;

    public Login() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        text = new JTextField("0");
        mainPanel.add(text, BorderLayout.NORTH);
        
    }
}