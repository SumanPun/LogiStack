package com.inventory.LogiStack.services.impl;

import com.inventory.LogiStack.enums.RoleEnum;
import com.inventory.LogiStack.repositories.RoleRepository;
import com.inventory.LogiStack.repositories.UserRepository;
import com.inventory.LogiStack.services.IUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class Utility implements IUtility {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<String> getUserRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            List<String> getRoles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return getRoles;
        }
        return null;
    }

    public boolean checkAdminRoleFromLoggedInUser(){
        List<String> userRoles = getUserRoles();
        return userRoles.contains(RoleEnum.ROLE_ADMIN.name());
    }

    public String getLoggedInUserName(){
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof UserDetails){
            UserDetails userDetails = (UserDetails) principle;
            return userDetails.getUsername();
        }
        return null;
    }

    @Override
    public String generateUniqueString() {
        UUID uuid = UUID.randomUUID();
        String guidString = uuid.toString().replace("-","");
        return guidString.substring(0,10);
    }

    @Override
    public LocalDateTime convertToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate conversionDate = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = conversionDate.atStartOfDay();
        return dateTime;
    }
}

