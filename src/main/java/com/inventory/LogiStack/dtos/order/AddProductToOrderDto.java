package com.inventory.LogiStack.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToOrderDto {
    private Long productId;
    private int quantity;
    private Double discount;
}
