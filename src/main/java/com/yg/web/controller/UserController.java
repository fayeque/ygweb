package com.yg.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.responses.ApiResponse;
import com.yg.web.dto.responses.GroupDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.userDto.AuthDto;
import com.yg.web.dto.userDto.UserDto;
import com.yg.web.entity.Group;
import com.yg.web.entity.User;
import com.yg.web.repository.UserRepository;
import com.yg.web.service.TransactionService;
import com.yg.web.service.UserService;
import com.yg.web.utils.JwtService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    UserRepository userInfoRepository;
    
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    
	 @PostMapping("/healthCheck")
	public ResponseEntity<String> healthCheck(){
		 logger.info("Health checking is workig fine here ");
		 return ResponseEntity.ok("Health check working fine");
	}
	
	 @PostMapping("/addUser")
	public ResponseEntity<SuccessResponseDto<User>> addUser(@RequestBody UserDto userDto ){
		 return userService.addUser(userDto);
	}
	 
	 @GetMapping("/hello")
	 public ResponseEntity<String> checkConn(){
		 logger.info("Health checking is workig fine here ");
		 return ResponseEntity.ok("Hello from ygweb server");
	 }
	 
	 @GetMapping("/getGroupCreatedByUser/{username}")
		public ResponseEntity<SuccessResponseDto<List<GroupDtoResponse>>> getGroupCreatedByUser(@PathVariable("username") String username){
			return userService.getGroupCreatedByUser(username);
	}
	   
	    @PostMapping("/authenticate")
	    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthDto authRequest) {
	    	System.out.println("inside authenticate");
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        if (authentication.isAuthenticated()) {
	        	System.out.println("inside authentication isAuthenticated");
//	        	return "token";
	            String tokenString= jwtService.generateToken(userDetails); 
	            Map<String, String> response = new HashMap<>();
	            response.put("token", tokenString);
	            return ResponseEntity.ok(response);
	        } else {
	            throw new UsernameNotFoundException("invalid user request !");
	        }
	    }
	    
	    @GetMapping("/validateToken")
	    public ResponseEntity<Map<String,String>> validateToken(){
            Map<String, String> response = new HashMap<>();
            response.put("isValid", "true");
            return ResponseEntity.ok(response);
	    }
	    
	    

}
