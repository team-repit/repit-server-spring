package com.repit.api.controller.auth;

import com.repit.api.service.auth.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth/login")
public class KakaoLoginPageController {

    @Value("${spring.kakao.client_id}")
    private String client_id;

    @Value("${spring.kakao.redirect_uri}")
    private String redirect_uri;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/kakao")
    public String kakaoCallback(@RequestParam(required = false) String code, 
                               @RequestParam(required = false) String error,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "카카오 로그인에 실패했습니다: " + error);
            return "login";
        }
        
        if (code != null) {
            try {
                // 1. 액세스 토큰 요청
                KakaoAuthService.KakaoTokenResponse tokenResponse = kakaoAuthService.getAccessToken(code);
                
                // 2. 사용자 정보 조회
                KakaoAuthService.KakaoUserInfo userInfo = kakaoAuthService.getUserInfo(tokenResponse.getAccessToken());
                
                // 3. 성공 메시지 표시
                String successMessage = String.format(
                    "카카오 로그인 성공!<br>" +
                    "사용자 ID: %d<br>" +
                    "닉네임: %s<br>" +
                    "이메일: %s<br>" +
                    "액세스 토큰: %s<br>" +
                    "리프레시 토큰: %s",
                    userInfo.getId(),
                    userInfo.getNickname() != null ? userInfo.getNickname() : "없음",
                    userInfo.getEmail() != null ? userInfo.getEmail() : "없음",
                    tokenResponse.getAccessToken().substring(0, 20) + "...",
                    tokenResponse.getRefreshToken() != null ? tokenResponse.getRefreshToken().substring(0, 20) + "..." : "없음"
                );
                
                model.addAttribute("success", successMessage);
                
            } catch (Exception e) {
                model.addAttribute("error", "카카오 로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "인증 코드를 받지 못했습니다.");
        }
        
        return "login";
    }
}