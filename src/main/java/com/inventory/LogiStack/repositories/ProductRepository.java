package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select s from Product s where s.id = ?1")
    Product getProduct(Long id);

    @Query("select s from Product s where s.isDeleted=false")
    List<Product> getAllProducts();

    @Query("select p from Product p where p.category.id = ?1 and p.isDeleted = false")
    List<Product> getProductsByCategory(Long categoryId);

    @Query("select p from Product p where p.supplier.id=?1 and p.isDeleted=false")
    List<Product> getProductsBySupplier(Long supplierId);
}
