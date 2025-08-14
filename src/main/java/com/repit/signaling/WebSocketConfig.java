package com.repit.signaling;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

// 시그널링 서버 구축
@Configuration
@EnableWebSocket // WebSocket 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SignalHandler signalHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalHandler, "/signal")
                .setAllowedOrigins("*");
    }
}