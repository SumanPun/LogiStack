package com.inventory.LogiStack.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
public class CreateCategoryDto{

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
}
