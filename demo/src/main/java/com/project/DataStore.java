package com.project;

import java.util.ArrayList;

public class DataStore {
    public static double currentBalance = 0.0;
    public static double maxBudget = 0;

    // ประวัติการซื้อ
    public static ArrayList<String> history = new ArrayList<>();
    public static ArrayList<String> historyCategories = new ArrayList<>();
    
    // Wishlist
    public static ArrayList<String> wishNames = new ArrayList<>();
    public static ArrayList<Double> wishPrices = new ArrayList<>();
    public static ArrayList<Integer> wishPriorities = new ArrayList<>();
    public static ArrayList<String> wishCategories = new ArrayList<>();

    // stats
    public static java.util.Map<String, Double> categoryTotals = new java.util.HashMap<>();
}