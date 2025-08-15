package com.repit.api.transport;

import com.corundumstudio.socketio.Configuration;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Slf4j
@org.springframework.context.annotation.Configuration
public class SocketIOConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private int port;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        // CORS 문제 해결을 위해 오리진 추가
        config.setOrigin("https://repit-web.vercel.app"); // 프론트엔드 배포 주소

        server = new SocketIOServer(config);
        return server;
    }

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
}