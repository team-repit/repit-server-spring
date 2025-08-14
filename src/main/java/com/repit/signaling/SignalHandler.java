package com.example.webrtc;

// import lombok.extern.slf4j.Slf4j; // Slf4j 임포트 삭제
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
// @Slf4j // @Slf4j 어노테이션 삭제
public class SignalHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (sessions.size() >= 2) {
            System.out.println("Connection denied: chat room is full. " + session.getId());
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Room is full"));
            return;
        }
        sessions.add(session);
        // log.info 대신 System.out.println 사용
        System.out.println("Client connected: " + session.getId() + ", current sessions: " + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                System.out.println("Relaying message from " + session.getId() + " to " + webSocketSession.getId());
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        // log.info 대신 System.out.println 사용
        System.out.println("Client disconnected: " + session.getId() + ", reason: " + status.getReason() + ". current sessions: " + sessions.size());
    }
}