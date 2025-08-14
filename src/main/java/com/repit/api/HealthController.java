package com.repit.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "헬스체크 관련 API")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "루트 경로 헬스체크", description = "로드밸런서 기본 헬스체크용")
    public String health() {
        return "OK";
    }

    @GetMapping("/health")
    @Operation(summary = "헬스체크 엔드포인트", description = "명시적인 헬스체크용")
    public String healthCheck() {
        return "OK";
    }
} 