package com.inventory.LogiStack.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal unit_price;
    private int quantity;
    private int reorder_level;
    private Long supplierId;
    private Long categoryId;
}
