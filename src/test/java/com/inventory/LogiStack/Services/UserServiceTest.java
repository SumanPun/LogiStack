package com.inventory.LogiStack.Services;

import com.inventory.LogiStack.config.AppConstants;
import com.inventory.LogiStack.dtos.UpdateUserDto;
import com.inventory.LogiStack.dtos.UserDto;
import com.inventory.LogiStack.entity.Role;
import com.inventory.LogiStack.entity.User;
import com.inventory.LogiStack.exceptions.ApiException;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.RoleRepository;
import com.inventory.LogiStack.repositories.UserRepository;
import com.inventory.LogiStack.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;
    private User testUser;
    private User testUpdateUser;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        testUser = new User();
        testUser.setId(1L);
        testUser.setActive(true);

        user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        testUpdateUser = new User();
        testUpdateUser.setId(1L);
        testUpdateUser.setFirstName("John");
        testUpdateUser.setLastName("Doe");
        testUpdateUser.setAge(22);
        testUpdateUser.setGender("Male");
        testUpdateUser.setAddress("Ktm");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Jane");
        updateUserDto.setLastName("Doe");
        updateUserDto.setGender("Female");
        updateUserDto.setAge(25);
        updateUserDto.setAddress("Btl");
    }


    @Test
    void testGetAllUsers(){

        User user1 = new User();
        user1.setFirstName("John");
        user1.setEmail("john@test.com");
        user1.setActive(true);

        User user2 = new User();
        user2.setFirstName("Doe");
        user2.setEmail("doe@test.com");
        user1.setActive(true);

        List<User> users = Arrays.asList(user1,user2);

        when(userRepository.findAllUsers()).thenReturn(users);

        UserDto userDto1 = new UserDto();
        userDto1.setFirstName("John");
        userDto1.setEmail("john@test.com");
        userDto1.setActive(true);

        UserDto userDto2 = new UserDto();
        userDto2.setFirstName("Doe");
        userDto2.setEmail("doe@test.com");
        userDto2.setActive(true);

        when(modelMapper.map(user1,UserDto.class)).thenReturn(userDto1);
        when(modelMapper.map(user2,UserDto.class)).thenReturn(userDto2);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("doe@test.com",result.get(1).getEmail());

        verify(userRepository, times(1)).findAllUsers();

    }

    @Test
    void testGetUserById_Success(){

        long userId = 1L;
        User user1 = new User();
        user1.setId(userId);
        user1.setFirstName("John");
        user1.setEmail("john@test.com");

        UserDto userDto1 = new UserDto();
        userDto1.setUserId(userId);
        userDto1.setFirstName("John");
        userDto1.setEmail("john@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        when(modelMapper.map(user1, UserDto.class)).thenReturn(userDto1);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("john@test.com",result.getEmail());

        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    void testGetUserById_ThrowsResourceNotFoundException(){

        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found with Id : 1", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testRegisterNewUser_UserDoesNotExist() {
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findById(AppConstants.NORMAL_USER)).thenReturn(Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.registerNewUser(userDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterNewUser_UserAlreadyExists() {
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new User()));

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.registerNewUser(userDto);
        });

        assertEquals("user already exist with email: test@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Ensure save is never called
    }

    @Test
    public void testRegisterNewUser_ExceptionHandling() {
        when(userRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Database error"));

        UserDto result = userService.registerNewUser(userDto);

        assertNull(result);
    }


    @Test
    public void testDeleteUser_UserExists(){

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        assertFalse(testUser.isActive());

        verify(userRepository).save(testUser);
    }

    @Test
    public void testDeleteUser_UserDoesNotExist(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("USER not found with ID : 1", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser_ExceptionHandling(){
        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        boolean result = userService.deleteUser(1L);

        assertFalse(result);
    }

    @Test
    public void testUpdateUser_UserExist(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUpdateUser));
        when(modelMapper.map(testUpdateUser,UserDto.class)).thenReturn(new UserDto());

        UserDto result = userService.updateUser(1L, updateUserDto);

        assertNotNull(result);
        assertEquals("Jane", testUpdateUser.getFirstName());
        assertEquals("Doe", testUpdateUser.getLastName());
        assertEquals(25, testUpdateUser.getAge());
        assertEquals("Female", testUpdateUser.getGender());
        assertEquals("Btl", testUpdateUser.getAddress());

        verify(userRepository).save(testUpdateUser);
    }
}
