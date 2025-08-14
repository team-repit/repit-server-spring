package com.repit.signaling;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

// 시그널링 서버 구축
@Configuration
@EnableWebSocket // WebSocket 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final com.example.webrtc.SignalHandler signalHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalHandler, "/signal") // "/signal" 경로에 핸들러 등록
                .setAllowedOrigins("*"); // 모든 출처에서 오는 요청을 허용 (CORS)
    }
}