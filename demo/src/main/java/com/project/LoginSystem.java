package com.project;

import java.util.Scanner;

public class LoginSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String correctUsername = "admin";
        String correctPassword = "1234";

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Exter password: ");
        String password = sc.nextLine();

        if (username.equals(correctUsername) && password.equals(correctPassword)) {
            System.out.println("Login successful!");
        }else {
            System.out.println("Login failed!");
        }
        sc.close();
    }
}
