package com.yg.web.utils;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserJwtDetails {
	public String username;
	
	public UserJwtDetails(String username) {
		this.username=username;
	}
	
}