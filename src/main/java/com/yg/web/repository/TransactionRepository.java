package com.yg.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yg.web.dto.dbToObjMapDto.MemberTransactionDTO;
import com.yg.web.entity.Group;
import com.yg.web.entity.Member;
import com.yg.web.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	public Transaction findById(Long id);

	public Integer deleteById(Long transactionId);
	
    @Query("SELECT t FROM Transaction t WHERE t.member = :member AND t.user_group = :user_group")
    List<Transaction> findByMemberAndUserGroup(@Param("member") Member member, @Param("user_group") Group user_group);
    
    @Query("SELECT t FROM Transaction t WHERE t.user_group = :user_group and year=:year")
    List<Transaction> findByUserGroup(@Param("user_group") Group user_group,@Param("year") int year);
    
//    @Query(value = "SELECT A.id AS memberId, A.username, A.name, C.id AS transactionId, C.amount, C.year, C.transactionType, C.transactionDate " +
//            "FROM members A " +
//            "JOIN group_members B ON A.id = B.member_id AND B.group_id = :groupId " +
//            "LEFT JOIN member_transactions C ON A.username = C.member_username AND B.group_id = C.group_id",
//    nativeQuery = true)
//    List<Object[]> findMembersWithTransactionsNative(@Param("groupId") Long groupId);

    
}
