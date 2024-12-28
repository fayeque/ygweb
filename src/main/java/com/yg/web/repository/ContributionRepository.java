package com.yg.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yg.web.entity.Contribution;
import com.yg.web.entity.Expense;


@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
	List<Contribution> findByGroupId(Long groupId);
}
