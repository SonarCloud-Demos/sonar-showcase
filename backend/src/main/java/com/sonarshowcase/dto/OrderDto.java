package com.sonarshowcase.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for creating orders.
 * Contains only the fields that should be user-settable.
 */
@Getter
@Setter
public class OrderDto {

    private Long userId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String notes;
    private List<Long> productIds;
}
