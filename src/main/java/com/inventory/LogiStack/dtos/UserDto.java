package com.inventory.LogiStack.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long userId;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Email(message = "invalid email")
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String gender;
    @NotEmpty
    private int age;
    @NotEmpty
    private String address;
    private LocalDateTime createdAt;
    private boolean isActive;

    private Set<RoleDto> roles = new HashSet<>();
}
