package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.dtos.CategoryDto;
import com.inventory.LogiStack.dtos.CreateCategoryDto;
import com.inventory.LogiStack.entity.Category;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.CategoryRepository;
import com.inventory.LogiStack.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CreateCategoryDto model) {
        Category category = new Category();
        category.setName(model.getName());
        category.setDescription(model.getDescription());
        category.setDeleted(false);
        category.setCreatedAt(LocalDateTime.now());
        this.categoryRepository.save(category);
        return this.modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map((category) -> this.modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findCategoryById(id);
        if(category != null){
            return this.modelMapper.map(category,CategoryDto.class);
        }else throw new ResourceNotFoundException("category","categoryId",id);
    }

    @Override
    public Boolean deleteCategoryById(Long id) {
        Category category = categoryRepository.findCategoryById(id);
        if(category != null){
            category.setDeleted(true);
            return true;
        }else throw new ResourceNotFoundException("category","categoryId",id);
    }

    @Override
    public CategoryDto updateCategory(Long id, CreateCategoryDto model) {
        Category category = categoryRepository.findCategoryById(id);
        if(category != null){
            category.setName(model.getName());
            category.setDescription(model.getDescription());
            category.setUpdatedAt(LocalDateTime.now());
            this.categoryRepository.save(category);
            return modelMapper.map(category,CategoryDto.class);
        }else throw new ResourceNotFoundException("category","categoryId",id);
    }
}
