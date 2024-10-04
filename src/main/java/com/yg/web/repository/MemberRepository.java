package com.yg.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yg.web.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	
	Optional<Member> findByUsername(String username);
	
	
    @Query(value = "SELECT COUNT(*) FROM group_members gm where gm.member_id=:memberId and gm.group_id=:groupId", nativeQuery = true)
    Long findMemberInGroup(@Param("memberId") Long memberId, @Param("groupId") Long groupId);
    
   

}
