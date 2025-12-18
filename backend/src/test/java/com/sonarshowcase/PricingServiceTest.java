package com.sonarshowcase;

import com.sonarshowcase.util.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for PricingService class.
 */
class PricingServiceTest {

    private PricingService pricingService;
    
    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }
    
    // ==================== calculateFinalPrice Tests ====================
    
    @Test
    @DisplayName("calculateFinalPrice should add tax for regular customer")
    void testCalculateFinalPrice_regularCustomer() {
        BigDecimal result = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 1);
        // 100 + 8.25% tax = 108.25
        assertEquals(0, new BigDecimal("108.25").compareTo(result));
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply VIP discount")
    void testCalculateFinalPrice_vipCustomer() {
        BigDecimal result = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "VIP", 1);
        // 100 * 0.80 = 80 + 8.25% = 86.60
        assertTrue(result.compareTo(new BigDecimal("80.00")) > 0);
        assertTrue(result.compareTo(new BigDecimal("100.00")) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply GOLD discount")
    void testCalculateFinalPrice_goldCustomer() {
        BigDecimal result = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "GOLD", 1);
        assertTrue(result.compareTo(new BigDecimal("100.00")) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply SILVER discount")
    void testCalculateFinalPrice_silverCustomer() {
        BigDecimal result = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "SILVER", 1);
        assertTrue(result.compareTo(new BigDecimal("100.00")) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply bulk discount for quantity > 10")
    void testCalculateFinalPrice_bulkDiscountSmall() {
        BigDecimal priceNoDiscount = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 1);
        BigDecimal priceWithDiscount = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 15);
        assertTrue(priceWithDiscount.compareTo(priceNoDiscount) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply additional discount for quantity > 50")
    void testCalculateFinalPrice_bulkDiscountMedium() {
        BigDecimal priceSmallBulk = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 15);
        BigDecimal priceLargeBulk = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 55);
        assertTrue(priceLargeBulk.compareTo(priceSmallBulk) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should apply maximum discount for quantity > 100")
    void testCalculateFinalPrice_bulkDiscountLarge() {
        BigDecimal priceMediumBulk = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 55);
        BigDecimal priceMaxBulk = pricingService.calculateFinalPrice(
            new BigDecimal("100.00"), "REGULAR", 150);
        assertTrue(priceMaxBulk.compareTo(priceMediumBulk) < 0);
    }
    
    @Test
    @DisplayName("calculateFinalPrice should enforce minimum price")
    void testCalculateFinalPrice_minimumPrice() {
        BigDecimal result = pricingService.calculateFinalPrice(
            new BigDecimal("0.50"), "VIP", 150);
        assertTrue(result.compareTo(new BigDecimal("0.99")) >= 0);
    }
    
    // ==================== calculateShipping Tests ====================
    
    @Test
    @DisplayName("calculateShipping should calculate base cost")
    void testCalculateShipping_baseCost() {
        double result = pricingService.calculateShipping(10.0, 100.0);
        // 10 * 0.50 + 100 * 0.02 = 5 + 2 = 7
        assertEquals(7.0, result, 0.01);
    }
    
    @Test
    @DisplayName("calculateShipping should add overweight surcharge")
    void testCalculateShipping_overweight() {
        double normalWeight = pricingService.calculateShipping(15.0, 100.0);
        double overweight = pricingService.calculateShipping(25.0, 100.0);
        // Overweight should add $5.00 surcharge (plus weight difference)
        assertTrue(overweight > normalWeight + 5.0);
    }
    
    @Test
    @DisplayName("calculateShipping should add long distance surcharge")
    void testCalculateShipping_longDistance() {
        double normalDistance = pricingService.calculateShipping(10.0, 400.0);
        double longDistance = pricingService.calculateShipping(10.0, 600.0);
        // Long distance should add $10.00 surcharge (plus distance difference)
        assertTrue(longDistance > normalDistance);
    }
    
    @Test
    @DisplayName("calculateShipping should multiply for very long distance")
    void testCalculateShipping_veryLongDistance() {
        double longDistance = pricingService.calculateShipping(10.0, 800.0);
        double veryLongDistance = pricingService.calculateShipping(10.0, 1200.0);
        // Very long distance should be 25% more
        assertTrue(veryLongDistance > longDistance * 1.2);
    }
    
    @Test
    @DisplayName("calculateShipping should enforce minimum cost")
    void testCalculateShipping_minimumCost() {
        double result = pricingService.calculateShipping(1.0, 10.0);
        assertTrue(result >= 3.99);
    }
    
    // ==================== applyCoupon Tests ====================
    
    @Test
    @DisplayName("applyCoupon should apply SAVE10 discount")
    void testApplyCoupon_save10() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "SAVE10");
        assertEquals(0, new BigDecimal("90.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply SAVE20 discount")
    void testApplyCoupon_save20() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "SAVE20");
        assertEquals(0, new BigDecimal("80.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply SAVE30 discount")
    void testApplyCoupon_save30() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "SAVE30");
        assertEquals(0, new BigDecimal("70.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply HALF discount")
    void testApplyCoupon_half() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "HALF");
        assertEquals(0, new BigDecimal("50.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply SUMMER2023 discount")
    void testApplyCoupon_summer2023() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "SUMMER2023");
        assertEquals(0, new BigDecimal("85.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply WINTER2023 discount")
    void testApplyCoupon_winter2023() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "WINTER2023");
        assertEquals(0, new BigDecimal("75.00").compareTo(result));
    }
    
    @Test
    @DisplayName("applyCoupon should apply NEWYEAR flat discount")
    void testApplyCoupon_newyear() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "NEWYEAR");
        assertEquals(new BigDecimal("75.00"), result);
    }
    
    @Test
    @DisplayName("applyCoupon should not affect price for FREESHIP")
    void testApplyCoupon_freeship() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "FREESHIP");
        assertEquals(new BigDecimal("100.00"), result);
    }
    
    @Test
    @DisplayName("applyCoupon should not affect price for invalid coupon")
    void testApplyCoupon_invalid() {
        BigDecimal result = pricingService.applyCoupon(new BigDecimal("100.00"), "INVALID");
        assertEquals(new BigDecimal("100.00"), result);
    }
    
    // ==================== calculateInstallments Tests ====================
    
    @Test
    @DisplayName("calculateInstallments should return correct number of payments")
    void testCalculateInstallments_correctCount() {
        double[] payments = pricingService.calculateInstallments(1200.0, 12);
        assertEquals(12, payments.length);
    }
    
    @Test
    @DisplayName("calculateInstallments should have no interest for 3 months or less")
    void testCalculateInstallments_noInterest() {
        double[] payments = pricingService.calculateInstallments(300.0, 3);
        assertEquals(100.0, payments[0], 0.01);
    }
    
    @Test
    @DisplayName("calculateInstallments should have 5% interest for 4-6 months")
    void testCalculateInstallments_lowInterest() {
        double[] payments = pricingService.calculateInstallments(600.0, 6);
        // 600 * 1.05 = 630 / 6 = 105
        assertEquals(105.0, payments[0], 0.01);
    }
    
    @Test
    @DisplayName("calculateInstallments should have 10% interest for 7-12 months")
    void testCalculateInstallments_mediumInterest() {
        double[] payments = pricingService.calculateInstallments(1200.0, 12);
        // 1200 * 1.10 = 1320 / 12 = 110
        assertEquals(110.0, payments[0], 0.01);
    }
    
    @Test
    @DisplayName("calculateInstallments should have 15% interest for over 12 months")
    void testCalculateInstallments_highInterest() {
        double[] payments = pricingService.calculateInstallments(2400.0, 24);
        // 2400 * 1.15 = 2760 / 24 = 115
        assertEquals(115.0, payments[0], 0.01);
    }
    
    @Test
    @DisplayName("calculateInstallments should have equal payments")
    void testCalculateInstallments_equalPayments() {
        double[] payments = pricingService.calculateInstallments(1200.0, 12);
        for (int i = 1; i < payments.length; i++) {
            assertEquals(payments[0], payments[i], 0.01);
        }
    }
}

