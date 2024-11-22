package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.dtos.SupplierDto;
import com.inventory.LogiStack.entity.Supplier;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.SupplierRepository;
import com.inventory.LogiStack.services.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierDto createSupplier(SupplierDto model) {
        Supplier supplier = new Supplier();
        supplier.setName(model.getName());
        supplier.setAddress(model.getAddress());
        supplier.setPhoneNo(model.getPhoneNo());
        supplier.setStatus(true);
        supplier.setCreatedAt(LocalDateTime.now());
        this.supplierRepository.save(supplier);
        return modelMapper.map(supplier,SupplierDto.class);
    }

    @Override
    public SupplierDto getSupplierById(Long id) {
        Supplier supplier = this.supplierRepository.getSupplier(id);
        if (supplier != null){
            return this.modelMapper.map(supplier,SupplierDto.class);
        }else throw new ResourceNotFoundException("Supplier","SupplierId",id);
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        List<Supplier> suppliers = this.supplierRepository.getAllSuppliers();
        return suppliers.stream().map((supplier -> this.modelMapper.map(supplier,SupplierDto.class))).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteSupplier(Long id) {
        Supplier supplier = this.supplierRepository.getSupplier(id);
        if (supplier != null){
            supplier.setStatus(false);
            supplier.setUpdatedAt(LocalDateTime.now());
            this.supplierRepository.save(supplier);
            return true;
        }else throw new ResourceNotFoundException("Supplier","SupplierId",id);
    }

    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto model) {
        Supplier supplier = this.supplierRepository.getSupplier(id);
        if (supplier != null){
            supplier.setName(model.getName());
            supplier.setAddress(model.getAddress());
            supplier.setPhoneNo(model.getPhoneNo());
            supplier.setUpdatedAt(LocalDateTime.now());
            this.supplierRepository.save(supplier);
            return this.modelMapper.map(supplier,SupplierDto.class);
        }else throw new ResourceNotFoundException("Supplier","SupplierId",id);
    }
}
