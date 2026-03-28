package com.project;
import java.util.ArrayList;

public class DataStore {
    public static double currentBalance = 0;
    public static double maxBudget = 0;

    // เพิ่ม List สำหรับเก็บประวัติการซื้อ
    public static ArrayList<String> history = new ArrayList<>(); 
    
    // wishlist
    public static ArrayList<String> wishNames = new ArrayList<>();
    public static ArrayList<Double> wishPrices = new ArrayList<>();
}
