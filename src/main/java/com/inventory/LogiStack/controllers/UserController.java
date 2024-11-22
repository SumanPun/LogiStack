package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.UpdateUserDto;
import com.inventory.LogiStack.dtos.UserDto;
import com.inventory.LogiStack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logistack/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long id){
        UserDto getUser = this.userService.getUserById(id);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable ("id") Long id, @RequestBody UpdateUserDto userDto){
        UserDto getUser = this.userService.updateUser(id,userDto);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> getUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(getUsers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable ("id") Long id) {
        boolean deleteUser = this.userService.deleteUser(id);
        if(deleteUser){
            ApiResponse userDeleted = new ApiResponse("user successfully deleted with user id : "+ id,true);
            return new ResponseEntity<>(userDeleted,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new ApiResponse("user is not deleted with user id : "+id,false),HttpStatus.BAD_REQUEST);
        }

    }

}
