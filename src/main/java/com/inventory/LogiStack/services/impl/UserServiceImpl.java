package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.config.AppConstants;
import com.inventory.LogiStack.dtos.AssignRoleRequest;
import com.inventory.LogiStack.dtos.UpdateUserDto;
import com.inventory.LogiStack.dtos.UserDto;
import com.inventory.LogiStack.entity.Role;
import com.inventory.LogiStack.entity.User;
import com.inventory.LogiStack.enums.RoleEnum;
import com.inventory.LogiStack.exceptions.ApiException;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.RoleRepository;
import com.inventory.LogiStack.repositories.UserRepository;
import com.inventory.LogiStack.services.IUtility;
import com.inventory.LogiStack.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IUtility utility;

    @Override
    public UserDto createUser(UserDto userDto) {
        try{

            User user = this.modelMapper.map(userDto,User.class);
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            return null;
        }catch (Exception ex){

            return null;
        }
    }

    @Override
    public UserDto updateUser(long id, UpdateUserDto userDto) {
        try{
            User user = this.userRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("user","userId",id));
            user.setAge(userDto.getAge());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setGender(userDto.getGender());
            user.setAddress(userDto.getAddress());
            this.userRepository.save(user);
            return this.modelMapper.map(user, UserDto.class);
        } catch (Exception e) {
            System.out.println(String.format("Error occurred while deleting user with userId {%s}", id));
            return null;
        }
    }

    @Override
    public UserDto getUserById(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",id));
        return this.modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAllUsers();
        List<UserDto> userDtos = users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public boolean deleteUser(long id) {
        try {
            User user = this.userRepository.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("USER","ID",id));
            user.setActive(false);
            this.userRepository.save(user);
            return true;
        }
        catch (ResourceNotFoundException ex){
            throw ex;
        }
        catch (Exception ex){
            System.out.println(String.format("Error occurred while deleting user with userId {%s}", id));
            return false;
        }
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        try{
            Optional<User> alreadyUser = this.userRepository.findByEmail(userDto.getEmail());
            System.out.println(alreadyUser);
            if(alreadyUser.isPresent()){
                throw new ApiException("user already exist with email: "+userDto.getEmail());
            }
            User user = this.modelMapper.map(userDto,User.class);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setActive(true);
            Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
            user.getRoles().add(role);
            User newUser = this.userRepository.save(user);
            return this.modelMapper.map(newUser,UserDto.class);
        }
        catch (ApiException ex){
            throw ex;
        }
        catch (Exception ex){
            System.out.println(String.format("Error occurred while creating user with email {%s}", userDto.getEmail()));
            return null;
        }

//        Optional<User> alreadyUser = this.userRepository.findByEmail(userDto.getEmail());
//        System.out.println(alreadyUser);
//        if(alreadyUser.isPresent()){
//            throw new ApiException("user already exist with email: "+userDto.getEmail());
//        }
//        User user = this.modelMapper.map(userDto,User.class);
//        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
//        user.setCreatedAt(LocalDateTime.now());
//        user.setActive(true);
//        Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
//        user.getRoles().add(role);
//        User newUser = this.userRepository.save(user);
//        return this.modelMapper.map(newUser,UserDto.class);

    }

    @Override
    public boolean assignUserRole(AssignRoleRequest request) {
        User user = this.userRepository.findByEmail(request.getUserName())
                .orElseThrow(()-> new ResourceNotFoundException("User","userName"+request.getUserName(),0));
        Role role = this.roleRepository.findRoleByName(request.getRoleName())
                .orElseThrow(()-> new ResourceNotFoundException("Role","roleName"+request.getRoleName(),0));
        boolean isLoggedUserAdmin = utility.checkAdminRoleFromLoggedInUser();
        if (isLoggedUserAdmin){
           user.getRoles().add(role);
           this.userRepository.save(user);
           return true;
        }

        return false;
    }

//    private boolean checkAdminRoleFromLoggedInUser(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(!(authentication instanceof AnonymousAuthenticationToken)){
//            List<String> currentUserRoles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
//            return currentUserRoles.contains(RoleEnum.ROLE_ADMIN.name());
//        }
//        return false;
//    }
}
