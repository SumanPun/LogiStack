package com.inventory.LogiStack.dtos.order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ConformOrderDto {

    @NotEmpty
    private String date;
}
