package com.inventory.LogiStack.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.LogiStack.controllers.UserController;
import com.inventory.LogiStack.dtos.ApiResponse;
import com.inventory.LogiStack.dtos.UpdateUserDto;
import com.inventory.LogiStack.dtos.UserDto;
import com.inventory.LogiStack.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        UserDto userDto = new UserDto();  // create a sample UserDto
        userDto.setUserId(userId);
        when(userService.getUserById(userId)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.getById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void updateUser_ShouldReturnUpdatedUserDto() throws Exception
//    {
//        long userId = 1L;
//        UpdateUserDto updateUserDto = new UpdateUserDto();
//        updateUserDto.setUserId(userId);
//        updateUserDto.setFirstName("John");
//        updateUserDto.setLastName("Doe");
//        updateUserDto.setAge(22);
//        updateUserDto.setGender("Male");
//        updateUserDto.setAddress("ktm");
//
//        UserDto userDto = new UserDto();
//        userDto.setUserId(userId);
//        userDto.setFirstName("John");
//        userDto.setLastName("Doe");
//        userDto.setAge(22);
//        userDto.setAddress("Male");
//        userDto.setAddress("ktm");
//
//        when(userService.updateUser(userId,updateUserDto)).thenReturn(userDto);
//
//        mockMvc.perform(put("/api/users/edit/{id}",userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateUserDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(userId))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.address").value("ktm"));
//    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteById_WithAdmin_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        UserDto userDto = new UserDto();  // create a sample UserDto
        userDto.setUserId(userId);
        when(userService.deleteUser(userId)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse> response = userController.deleteUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse apiResponse = response.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("user successfully deleted with user id : " + userId, apiResponse.getMessage());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @WithMockUser(roles = "NORMAL")
    void deleteById_WithNormalRole_ShouldThrowAccessDeniedException() throws Exception {
        mockMvc.perform(delete("/api/v1/logistack/users/delete/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getById_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.getById(userId));
        verify(userService, times(1)).getUserById(userId);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_WithAdminRole_ShouldReturnAllUsers() {
        // Arrange
        List<UserDto> users = new ArrayList<>();  // create a sample list of users
        users.add(new com.inventory.LogiStack.dtos.UserDto());
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "NORMAL") // Simulates a user with the NORMAL role
    void getAllUsers_WithNormalRole_ShouldThrowAccessDeniedException() throws Exception {
        mockMvc.perform(get("/api/v1/logistack/users/")) // Perform a GET request
                .andExpect(status().isForbidden());
    }
}
