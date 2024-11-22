package com.inventory.LogiStack.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JwtAuthRequest {

    @Email(message = "Enter a valid email!!")
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
