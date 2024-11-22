package com.inventory.LogiStack.dtos;

import com.inventory.LogiStack.entity.Order;
import com.inventory.LogiStack.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto{
    private Long id;
    private int quantity;
    private BigDecimal totalPrice;
    private BigDecimal unitPrice;
    private Double discount;
}
