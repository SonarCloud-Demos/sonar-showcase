package com.sonarshowcase.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with dead code and poor practices
 * 
 * REL-06: Dead code - unreachable statements
 * MNT-06: Poor naming conventions
 */
public class Utils {

    // MNT: Poor naming
    public static String data1;
    public static String data2;
    public static int temp;
    public static Object x;
    
    /**
     * MNT: Poor method implementation
     */
    public static String doStuff(String input) {
        if (input == null) {
            return "null input";
        }
        
        String result = input.toUpperCase();
        
        // MNT: Unnecessary variable reassignment
        String temp = result;
        result = temp;
        
        // MNT: Debug print left in code
        System.out.println("Processing: " + result);
        
        return result;
    }
    
    /**
     * MNT: Tautological condition - always true
     */
    public static int calculate(int a, int b) {
        int result = a + b;
        
        // MNT: Condition is always true - SonarQube will flag this
        boolean alwaysTrue = true;
        if (alwaysTrue) {
            return result;
        }
        
        // MNT: This code is effectively dead but compiles
        result = a * b;
        return result;
    }
    
    /**
     * MNT: Poor variable naming
     */
    public static String processData(String s, int n, boolean f) {
        String temp1 = s;
        String temp2 = "";
        int i = 0;
        
        // MNT: Single letter variables, unclear logic
        for (int j = 0; j < n; j++) {
            if (f) {
                temp2 += temp1.charAt(i % temp1.length());
                i++;
            } else {
                temp2 += temp1.charAt(n - j - 1);
            }
        }
        
        return temp2;
    }
    
    /**
     * MNT: Confusing method that does nothing useful
     */
    public static void doSomething(Object obj) {
        if (obj != null) {
            Object temp = obj;
            obj = null;
            temp = temp.toString();
            System.out.println(temp);
        }
    }
    
    /**
     * MNT: Method with misleading name
     */
    public static List<String> getUsers() {
        // MNT: Returns empty list, name suggests it gets users
        return new ArrayList<>();
    }
    
    /**
     * MNT: Verbose loop implementation
     */
    public static void processItems(List<String> items) {
        // MNT: Could use forEach or streams
        int i = 0;
        while (i < items.size()) {
            String item = items.get(i);
            System.out.println(item); // MNT: Debug output
            i++; // MNT: Manual increment, error-prone
        }
    }
    
    /**
     * MNT: Overly complex for simple task
     */
    public static boolean isPositive(int number) {
        if (number > 0) {
            if (number != 0) {
                if (number >= 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * MNT: Duplicate code
     */
    public static String formatName1(String first, String last) {
        if (first == null) first = "";
        if (last == null) last = "";
        return first.trim() + " " + last.trim();
    }
    
    /**
     * MNT: Duplicate of above
     */
    public static String formatName2(String first, String last) {
        if (first == null) first = "";
        if (last == null) last = "";
        return first.trim() + " " + last.trim();
    }
    
    /**
     * MNT: Another duplicate
     */
    public static String formatFullName(String firstName, String lastName) {
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
        return firstName.trim() + " " + lastName.trim();
    }
    
    // MNT: Unused private method
    private static void helperMethod() {
        System.out.println("Never called");
    }
    
    // MNT: Another unused method
    private static int anotherHelper(int x) {
        return x * 2;
    }
}

