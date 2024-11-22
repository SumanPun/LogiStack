package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.dtos.ProductDto;
import com.inventory.LogiStack.entity.Category;
import com.inventory.LogiStack.entity.Product;
import com.inventory.LogiStack.entity.Supplier;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.CategoryRepository;
import com.inventory.LogiStack.repositories.ProductRepository;
import com.inventory.LogiStack.repositories.SupplierRepository;
import com.inventory.LogiStack.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public ProductDto createProduct(ProductDto model) {
        Category category = categoryRepository.findCategoryById(model.getCategoryId());
        if (category == null) {
            throw new ResourceNotFoundException("Category", "CategoryId", model.getCategoryId());
        }
        Supplier supplier = supplierRepository.findById(model.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "SupplierId", model.getSupplierId()));
        Product product = new Product();
        product.setName(model.getName());
        product.setQuantity(model.getQuantity());
        product.setCreatedAt(LocalDateTime.now());
        product.setDescription(model.getDescription());
        product.setDeleted(false);
        product.setUnit_price(model.getUnit_price());
        product.setCategory(category);
        product.setReorder_level(model.getReorder_level());
        product.setSupplier(supplier);
        this.productRepository.save(product);
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", id));
        return this.modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = this.productRepository.getAllProducts();
        return products.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteProduct(Long id) {
        Product product = getProduct(id);
        product.setDeleted(true);
        this.productRepository.save(product);
        return true;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto model) {
        Product product = getProduct(id);
        product.setUpdatedAt(LocalDateTime.now());
        product.setName(model.getName());
        product.setQuantity(model.getQuantity());
        product.setDescription(model.getDescription());
        product.setUnit_price(model.getUnit_price());
        product.setReorder_level(model.getReorder_level());
        this.productRepository.save(product);
        return this.modelMapper.map(product, ProductDto.class);
    }

    private Product getProduct(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", id));
        return product;
    }

    @Override
    public List<ProductDto> getProductBySupplier(Long supplierId) {
        Supplier supplier = this.supplierRepository.findById(supplierId)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier","SupplierId",supplierId));
        List<Product> products = this.productRepository.getProductsBySupplier(supplierId);
        return products.stream().map(product -> this.modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductByCategory(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Cateogry", "CategoryId", categoryId));
        List<Product> products = this.productRepository.getProductsByCategory(categoryId);
        return products.stream().map(product -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }
}
