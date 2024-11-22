package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.SupplierDto;
import com.inventory.LogiStack.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistack/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/")
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto model) {
        SupplierDto saveSupplier = this.supplierService.createSupplier(model);
        return new ResponseEntity<>(saveSupplier, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SupplierDto>> getCategories() {
        List<SupplierDto> allCategories = this.supplierService.getAllSuppliers();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable(name = "id") Long id, @RequestBody SupplierDto model) {
        SupplierDto update = this.supplierService.updateSupplier(id, model);
        return new ResponseEntity<>(update, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable(name = "id") Long id) {
        SupplierDto Supplier = this.supplierService.getSupplierById(id);
        return new ResponseEntity<>(Supplier, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSupplierById(@PathVariable(name = "id") Long id) {
        boolean deleteSupplier = this.supplierService.deleteSupplier(id);
        if (deleteSupplier) {
            ApiResponse SupplierDeleted = new ApiResponse("supplier successfully deleted with supplier id : " + id, true);
            return new ResponseEntity<>(SupplierDeleted, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("supplier is not deleted with supplier id : " + id, false), HttpStatus.BAD_REQUEST);
        }
    }
}