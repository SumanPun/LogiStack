package com.inventory.LogiStack.dtos;

import com.inventory.LogiStack.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends BaseDto<Long> {
    private String name;
    private String description;
    private boolean isDeleted;
    private List<Product> products;
}
