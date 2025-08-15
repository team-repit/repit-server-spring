package com.repit.api.websocket.config;

import com.corundumstudio.socketio.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.SocketIOServer;

@Slf4j
@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    // application.yml 파일의 host, port 값 가져와서 자동 바인딩
    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        // CORS 허용
        config.setOrigin("*");
        return new SocketIOServer(config);
    }
}
/*
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Starting Socket.IO server...");
        server.start();
        log.info("Socket.IO server started at {}:{}", host, port);
    }

    @PreDestroy
    public void stopServer() {
        if (server != null) {
            log.info("Stopping Socket.IO server...");
            server.stop();
        }
    }

 */
