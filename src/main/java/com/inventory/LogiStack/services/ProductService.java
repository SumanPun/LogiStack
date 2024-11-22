package com.inventory.LogiStack.services;

import com.inventory.LogiStack.dtos.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto model);
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    Boolean deleteProduct(Long id);
    ProductDto updateProduct(Long id, ProductDto model);
    List<ProductDto> getProductBySupplier(Long supplierId);
    List<ProductDto> getProductByCategory(Long categoryId);
}
