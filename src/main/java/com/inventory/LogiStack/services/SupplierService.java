package com.inventory.LogiStack.services;

import com.inventory.LogiStack.dtos.SupplierDto;

import java.util.List;

public interface SupplierService {

    SupplierDto createSupplier(SupplierDto model);
    SupplierDto getSupplierById(Long id);
    List<SupplierDto> getAllSuppliers();
    Boolean deleteSupplier(Long id);
    SupplierDto updateSupplier(Long id, SupplierDto model);
}
