package com.yg.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yg.web.dto.responses.ApiResponse;
import com.yg.web.dto.responses.GroupDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.userDto.UserDto;
import com.yg.web.entity.Group;
import com.yg.web.entity.User;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.UserRepository;
import com.yg.web.service.utils.BuildResponseUtils;
import com.yg.web.service.utils.UserUtils;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	UserUtils userUtils;
	
	@Autowired
	BuildResponseUtils buildResponseUtils;
	
	
	public ResponseEntity<SuccessResponseDto<User>> addUser(UserDto userDto){
		if(userUtils.isUserExist(userDto.getUsername())) {
			throw new ResourceNotFoundException("username already exist");
		}
		User user = new User();
		user.setName(userDto.getName());
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(user);
		return buildResponseUtils.buildSuccessResponseDto("user addedd successfully",user);
	}
	


	public ResponseEntity<SuccessResponseDto<List<GroupDtoResponse>>> getGroupCreatedByUser(String username) {
		User user = userRepository.findByUsername(username);
//		
//	    if (user == null) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//	    }
//	    
//	    System.out.println("user here is " + user);
	    
	    List<Group> groups = user.getGroups();
	    
	    List<GroupDtoResponse> groupDtoResponses= groups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
         
         return buildResponseUtils.buildSuccessResponseDto("Groups created by user", groupDtoResponses);
	    
	}
	
    private GroupDtoResponse convertToDto(Group group) {
        GroupDtoResponse dto = new GroupDtoResponse();
        dto.setGroupId(group.getGroupId());
        dto.setGroupName(group.getGroupName());
        return dto;
    }

}
