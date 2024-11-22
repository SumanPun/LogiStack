package com.inventory.LogiStack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supplier")
public class Supplier extends BaseEntity<Long>{
    private String name;
    private String address;
    private String phoneNo;
    private boolean status;
    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
