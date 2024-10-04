package com.yg.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.yg.web.entity.User;
import com.yg.web.repository.UserRepository;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    		System.out.println("inside loadUserByUseranem " + username);
    	     Optional<User> userInfo = Optional.ofNullable(userRepository.findByUsername(username));
//    	     System.out.println("inside loadUserByUseranem userInfo " + userInfo);
    	        return userInfo.map(UserInfoUserDetails::new)
    	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
        
    }
}

