package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.CategoryDto;
import com.inventory.LogiStack.dtos.CreateCategoryDto;
import com.inventory.LogiStack.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistack/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto model){
        CategoryDto saveCategory = this.categoryService.createCategory(model);
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getCategories(){
        List<CategoryDto> allCategories = this.categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(name = "id") Long id, @RequestBody CreateCategoryDto model){
        CategoryDto update = this.categoryService.updateCategory(id,model);
        return new ResponseEntity<>(update, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "id") Long id){
        CategoryDto category = this.categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable(name = "id") Long id){
        boolean deleteCategory = this.categoryService.deleteCategoryById(id);
        if(deleteCategory){
            ApiResponse categoryDeleted = new ApiResponse("category successfully deleted with category id : "+ id,true);
            return new ResponseEntity<>(categoryDeleted,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ApiResponse("category is not deleted with category id : "+id,false),HttpStatus.BAD_REQUEST);
        }
    }
}
