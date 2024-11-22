package com.inventory.LogiStack.controllers;

import com.inventory.LogiStack.dtos.JwtAuthRequest;
import com.inventory.LogiStack.dtos.JwtAuthResponse;
import com.inventory.LogiStack.dtos.UserDto;
import com.inventory.LogiStack.exceptions.ApiException;
import com.inventory.LogiStack.security.JwtTokenHelper;
import com.inventory.LogiStack.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logistack/auth")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthRequest request){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        this.authenticate(request.getEmail(), request.getPassword());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String userName, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,password);
        try{
            this.authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException ex){
            System.out.println("bad credential exception");
            throw new ApiException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
        UserDto registerUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<>(registerUser,HttpStatus.CREATED);
    }
}
