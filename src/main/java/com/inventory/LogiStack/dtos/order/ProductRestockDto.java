package com.inventory.LogiStack.dtos.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRestockDto {

    @NotEmpty(message = "product id is required")
    private Long id;
    @NotEmpty(message = "quantity is required")
    private int quantity;
}
