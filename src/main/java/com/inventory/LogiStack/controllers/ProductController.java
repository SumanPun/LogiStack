package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.ProductDto;
import com.inventory.LogiStack.dtos.order.ProductRestockDto;
import com.inventory.LogiStack.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistack/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto model){
        ProductDto saveProduct = this.productService.createProduct(model);
        return new ResponseEntity<>(saveProduct, HttpStatus.CREATED);
    }
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getCategories(){
        List<ProductDto> allCategories = this.productService.getAllProducts();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductDto model){
        ProductDto update = this.productService.updateProduct(id,model);
        return new ResponseEntity<>(update, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "id") Long id){
        ProductDto Product = this.productService.getProductById(id);
        return new ResponseEntity<>(Product, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable(name = "id") Long id){
        boolean deleteProduct = this.productService.deleteProduct(id);
        if(deleteProduct){
            ApiResponse ProductDeleted = new ApiResponse("Product successfully deleted with Product id : "+ id,true);
            return new ResponseEntity<>(ProductDeleted,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ApiResponse("Product is not deleted with Product id : "+id,false),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/restock")
    public ResponseEntity<ApiResponse> restockProduct(@Valid @RequestBody ProductRestockDto model){
        boolean deleteProduct = this.productService.restockProduct(model);
        if(deleteProduct){
            ApiResponse ProductDeleted = new ApiResponse("Product restock successfully",true);
            return new ResponseEntity<>(ProductDeleted,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ApiResponse("Product cannot be restock",false),HttpStatus.BAD_REQUEST);
        }
    }
}
