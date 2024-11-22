package com.inventory.LogiStack.services;

import com.inventory.LogiStack.dtos.AssignRoleRequest;
import com.inventory.LogiStack.dtos.UpdateUserDto;
import com.inventory.LogiStack.dtos.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(long id, UpdateUserDto userDto);
    UserDto getUserById(long id);
    List<UserDto> getAllUsers();
    boolean deleteUser(long id);
    UserDto registerNewUser(UserDto userDto);
    boolean assignUserRole(AssignRoleRequest request);

}
