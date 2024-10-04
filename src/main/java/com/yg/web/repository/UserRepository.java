package com.yg.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yg.web.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
}
