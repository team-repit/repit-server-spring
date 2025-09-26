package com.repit.api.service.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoAuthService {

    @Value("${spring.kakao.client_id}")
    private String clientId;

    @Value("${spring.kakao.redirect_uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 인증 코드로 액세스 토큰을 요청합니다.
     */
    public KakaoTokenResponse getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(url, request, KakaoTokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("카카오 액세스 토큰 요청 실패: " + e.getMessage());
        }
    }

    /**
     * 액세스 토큰으로 사용자 정보를 조회합니다.
     */
    public KakaoUserInfo getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(url, HttpMethod.GET, request, KakaoUserInfo.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패: " + e.getMessage());
        }
    }

    @Data
    public static class KakaoTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("refresh_token")
        private String refreshToken;
        
        @JsonProperty("token_type")
        private String tokenType;
        
        @JsonProperty("expires_in")
        private int expiresIn;
        
        @JsonProperty("scope")
        private String scope;
    }

    @Data
    public static class KakaoUserInfo {
        private Long id;
        private String connected_at;
        private Map<String, Object> properties;
        private Map<String, Object> kakao_account;
        
        public String getNickname() {
            if (kakao_account != null && kakao_account.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
                return (String) profile.get("nickname");
            }
            return null;
        }
        
        public String getEmail() {
            if (kakao_account != null) {
                return (String) kakao_account.get("email");
            }
            return null;
        }
    }
}
