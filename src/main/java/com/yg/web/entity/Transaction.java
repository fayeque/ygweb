package com.yg.web.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import com.yg.web.enums.CollectionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_transactions") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private Integer amount;
  
    @Enumerated(EnumType.STRING)
    private CollectionType transactionType;
    
    private String transactionPeriod;
    private LocalDateTime transactionDate;
    private String addedBy;
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "group_id",referencedColumnName = "groupId")
    private Group user_group; 
    
    @ManyToOne
    @JoinColumn(name = "member_username",referencedColumnName = "username")
    private Member member; 
    
    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now(); 
    }

}
