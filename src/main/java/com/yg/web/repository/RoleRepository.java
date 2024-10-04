package com.yg.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yg.web.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	@Query(value = "select * from roles r1 where r1.group_id = :groupId and r1.username = :username",nativeQuery = true)
	public Role findByGroupAndUsername(@Param("groupId") Long groupId, @Param("username") String username);
	
}
