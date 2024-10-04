package com.yg.web.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.userDto.AuthDto;
import com.yg.web.dto.userDto.UserDto;
import com.yg.web.repository.UserRepository;
import com.yg.web.utils.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    UserRepository userInfoRepository;
    
    
//    @Autowired
//    UserNotificationRepository userNotificationRepository;
    
//    @Autowired
//    ShopkeeperNotificationRepository shopkeeperNotificationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    PasswordEncoder passwordEncoder;



   
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthDto authRequest) {
    	System.out.println("inside authenticate");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        UserJwtDetails userJwtDetails = new UserJwtDetails();
//        userJwtDetails.username = userDetails.getUsername();
//        userJwtDetails.rolesAuthorities = userDetails.getAuthorities();
//        System.out.println(userJwtDetails.username);
//        System.out.println(userJwtDetails.rolesAuthorities);
        if (authentication.isAuthenticated()) {
        	System.out.println("inside authentication isAuthenticated");
            return jwtService.generateToken(userDetails); 
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
     

}

