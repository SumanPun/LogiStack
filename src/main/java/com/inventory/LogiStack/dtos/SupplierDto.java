package com.inventory.LogiStack.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNo;
}
