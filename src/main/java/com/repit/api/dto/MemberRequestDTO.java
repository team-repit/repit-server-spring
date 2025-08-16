package com.repit.api.dto;

        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDTO {
    private String oauthId;
    private String oauthProvider;
    private String name;
    private String email;
}