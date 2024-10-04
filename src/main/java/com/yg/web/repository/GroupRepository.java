package com.yg.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yg.web.entity.Group;
import com.yg.web.entity.User;


public interface GroupRepository extends JpaRepository<Group,Long> {
	Optional<Group> findById(Long groupId);
	boolean existsByGroupNameAndUser(String groupName, User user);
	
    @Modifying
    @Query("UPDATE Group e SET e.totalAmount = :newValue WHERE e.id = :id")
    int updateYourColumnById(@Param("newValue") int newValue, @Param("id") Long id);

}
