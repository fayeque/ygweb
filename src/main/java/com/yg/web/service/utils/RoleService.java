package com.yg.web.service.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yg.web.entity.Role;
import com.yg.web.repository.RoleRepository;

@Service
public class RoleService {
	
    @Autowired
    private RoleRepository roleRepository;

    public boolean hasRole(Long groupId, String username) {
        // Assuming you have a method like findByUsernameAndRoleName in your repository
        Optional<Role> role= Optional.ofNullable(roleRepository.findByGroupAndUsername(groupId, username));
        return role.isPresent();
        
    }

}
