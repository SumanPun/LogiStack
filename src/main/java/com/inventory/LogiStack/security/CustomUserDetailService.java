package com.inventory.LogiStack.security;

import com.inventory.LogiStack.entity.User;
import com.inventory.LogiStack.exceptions.ResourceNotFoundException;
import com.inventory.LogiStack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("user","email",0));

        return user;
    }
}
