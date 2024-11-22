package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

    @Query("select s from Supplier s where s.id = ?1")
    Supplier getSupplier(Long id);

    @Query("select s from Supplier s where s.status=true")
    List<Supplier> getAllSuppliers();
}
