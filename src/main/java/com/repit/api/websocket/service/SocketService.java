package com.repit.api.websocket.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SocketService {
    @Autowired
    private final SocketIOServer server;


}
