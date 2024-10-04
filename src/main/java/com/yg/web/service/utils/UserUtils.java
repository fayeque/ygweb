package com.yg.web.service.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.stereotype.Service;

import com.yg.web.entity.User;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.UserRepository;


@Service
public class UserUtils {
	
	@Autowired
	UserRepository userRepository;
	
	public User findByUsername(String username) {
		User user =Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow(() -> new ResourceNotFoundException("Username Not Exist"));
		return user;
	}
	
	public boolean isUserExist(String username) {
	User user =Optional.ofNullable(userRepository.findByUsername(username)).orElse(null);
	if(user == null) {
		return false;
	}else {
		return true;
	}
   
	 
		
	}

}
