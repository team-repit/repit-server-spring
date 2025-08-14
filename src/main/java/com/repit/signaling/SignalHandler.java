package com.repit.signaling;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SignalHandler extends TextWebSocketHandler {

    // 1대1 통신이므로 세션을 2개만 관리합니다.
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (sessions.size() >= 2) {
            // 이미 2명이 연결된 경우, 더 이상 연결을 받지 않음
            log.warn("Connection denied: chat room is full.");
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Room is full"));
            return;
        }
        sessions.add(session);
        log.info("Client connected: {}, current sessions: {}", session.getId(), sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지를 보낸 세션을 제외한 다른 모든 세션에게 메시지를 전달합니다.
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                log.info("Relaying message from {} to {}", session.getId(), webSocketSession.getId());
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("Client disconnected: {}, reason: {}. current sessions: {}", session.getId(), status.getReason(), sessions.size());
    }
}