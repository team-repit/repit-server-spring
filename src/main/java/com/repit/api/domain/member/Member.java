package com.repit.api.domain.member;

import java.time.LocalDateTime;

/**
 * 회원 도메인
 */
public class Member {
    private final long member_id;
    private final String oauth_id;
    private final String oauth_provider;
    private final String name;
    private final String email;
    private final LocalDateTime created_at;
    private final LocalDateTime last_login;

    public Member(long member_id, String oauth_id, String oauth_provider, String name, 
                 String email, LocalDateTime created_at, LocalDateTime last_login) {
        this.member_id = member_id;
        this.oauth_id = oauth_id;
        this.oauth_provider = oauth_provider;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
        this.last_login = last_login;
    }

    // Getters
    public long getMember_id() { return member_id; }
    public String getOauth_id() { return oauth_id; }
    public String getOauth_provider() { return oauth_provider; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDateTime getCreated_at() { return created_at; }
    public LocalDateTime getLast_login() { return last_login; }
}
