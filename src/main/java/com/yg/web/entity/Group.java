package com.yg.web.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yg.web.enums.CollectionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_groups") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupName;
    
    private int totalAmount;
    
    @Enumerated(EnumType.STRING)
    private CollectionType collectionType;
     
    private Integer colletionAmount = 0;
    
    private LocalDateTime createdAt;
    
    private Integer totalExpense=0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id",referencedColumnName = "id")
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members;
    
    @OneToMany(mappedBy = "user_group", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();
    
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); 
    }
}
