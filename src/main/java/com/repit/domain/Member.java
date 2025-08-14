package com.repit.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "member") // Assuming table name is 'member'
@EntityListeners(AuditingEntityListener.class) // For @CreatedDate
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "oauth_id", length = 100, nullable = false, unique = true)
    private String oauthId;

    @Column(name = "oauth_provider", length = 20, nullable = false)
    private String oauthProvider;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @CreatedDate
    @Column(name = "created_at", updatable = false) // Not updatable after creation
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // You might want to add a constructor for easy object creation
    public Member(String oauthId, String oauthProvider, String name, String email) {
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.name = name;
        this.email = email;
    }
}
