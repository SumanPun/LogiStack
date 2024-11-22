package com.inventory.LogiStack.services;

import com.inventory.LogiStack.dtos.CategoryDto;
import com.inventory.LogiStack.dtos.CreateCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CreateCategoryDto model);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    Boolean deleteCategoryById(Long id);
    CategoryDto updateCategory(Long id, CreateCategoryDto model);
}
