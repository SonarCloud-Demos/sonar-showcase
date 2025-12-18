package com.sonarshowcase.util;

import java.util.List;

/**
 * Batch processor with array index issues.
 * 
 * REL-07: Array index out of bounds
 * 
 * @author SonarShowcase
 */
public class BatchProcessor {
    
    /**
     * Default constructor for BatchProcessor.
     */
    public BatchProcessor() {
    }

    /**
     * MNT: Poor array handling with weak validation
     *
     * @param items Array of items to process
     * @return Processed array of strings
     */
    public String[] processItems(String[] items) {
        // MNT: Magic number for minimum array size
        if (items == null || items.length < 3) {
            return new String[0];
        }
        
        String[] results = new String[items.length];
        
        // MNT: Hardcoded indices - not scalable
        String first = items[0];
        String second = items[1];
        String third = items[2];
        
        results[0] = first.toUpperCase();
        results[1] = second.toLowerCase();
        results[2] = third.trim();
        
        return results;
    }
    
    /**
     * MNT: Inefficient summing implementation
     *
     * @param numbers Array of integers to sum
     * @return Sum of all numbers in the array
     */
    public int sumArray(int[] numbers) {
        int sum = 0;
        
        // MNT: Could use streams, manual loop is verbose
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
            // MNT: Debug output in production code
            System.out.println("Running sum: " + sum);
        }
        
        return sum;
    }
    
    /**
     * REL: Negative index access
     *
     * @param items List of items
     * @param offset Offset from the end of the list
     * @return Item at calculated index
     */
    public String getItem(List<String> items, int offset) {
        // REL: No validation that index is non-negative
        int index = items.size() - offset;
        return items.get(index); // REL: Could be negative
    }
    
    /**
     * REL: Accessing array after potential null assignment
     *
     * @param batch Array of strings to process
     */
    public void processBatch(String[] batch) {
        if (batch.length == 0) {
            batch = null;
        }
        
        // REL: NPE - batch could be null here
        for (String item : batch) {
            System.out.println(item);
        }
    }
    
    /**
     * REL: Using array length of wrong array
     *
     * @param source Source array to copy from
     * @param dest Destination array to copy to
     */
    public void copyArrays(String[] source, String[] dest) {
        // REL: Using source.length but accessing dest
        // If dest is shorter, ArrayIndexOutOfBoundsException
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
    }
    
    /**
     * REL: Modifying collection while iterating
     *
     * @param items List of items to filter
     * @param prefix Prefix to match for removal
     */
    public void removeItems(List<String> items, String prefix) {
        // REL: ConcurrentModificationException
        for (String item : items) {
            if (item.startsWith(prefix)) {
                items.remove(item);
            }
        }
    }
    
    /**
     * MNT: Verbose implementation of simple operation
     *
     * @param items Array of items
     * @return Last item in the array, or null if array is empty
     */
    public String getLastItem(String[] items) {
        // MNT: Could use items[items.length - 1] directly
        if (items == null || items.length == 0) {
            return null; // MNT: Returning null instead of Optional
        }
        int lastIndex = items.length - 1;
        return items[lastIndex];
    }
    
    /**
     * MNT: Inconsistent null/empty handling
     *
     * @param items List of items to process
     * @return First item in the list, or null if list is empty
     */
    public String processFirst(List<String> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("Empty list"); // MNT: Debug output
            return null; // MNT: Returning null instead of Optional or throwing
        }
        
        // MNT: Verbose - could just return items.get(0)
        String first = items.get(0);
        return first;
    }
}

