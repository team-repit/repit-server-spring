package com.repit.api.dto;

import com.repit.api.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private Long memberId;
    private String oauthId;
    private String oauthProvider;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public MemberResponseDTO(Member member) {
        this.memberId = member.getMemberId();
        this.oauthId = member.getOauthId();
        this.oauthProvider = member.getOauthProvider();
        this.name = member.getName();
        this.email = member.getEmail();
        this.createdAt = member.getCreatedAt();
        this.lastLogin = member.getLastLogin();
    }
}