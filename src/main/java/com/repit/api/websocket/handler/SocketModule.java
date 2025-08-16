package com.repit.api.websocket.handler;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.repit.api.websocket.dto.SignalDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule implements ApplicationListener<ApplicationReadyEvent> {
    private final SocketIOServer server;

    // 생성자
    // SocketIOConfig 에서 만든 서버 정보대로 새로운 서버 생성
    public SocketModule(SocketIOServer server) {
        this.server = server;

        log.info("SocketModule Bean 생성 및 리스너 등록 완료!");

        // event listener
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("join_room", String.class, onJoinRoom());
        server.addEventListener("signal", SignalDto.class, onSignal());
    }

    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("🚀 애플리케이션 준비 완료, Socket.IO 서버를 시작합니다...");
        server.start();
    }

    /*
     * 함수 모음
     */

    // 클라이언트가 처음 접속했을 때 실행될 리스너
    private ConnectListener onConnected() {
        return client -> log.info("Socket[{}] - 접속 성공", client.getSessionId());
    }

    // 클라이언트와 접속이 끊겼을 때 실행될 리스너
    private DisconnectListener onDisconnected() {
        return client -> log.info("Socket[{}] - 접속 끊김", client.getSessionId());
    }

    private DataListener<String> onJoinRoom() {
        return (client, roomName, ackSender) -> {
            log.info("Socket[{}] - 방 참여: {}", client.getSessionId(), roomName);
            client.joinRoom(roomName);
        };
    }

    private DataListener<SignalDto> onSignal() {
        return (client, data, ackSender) -> {
            String room = data.getRoom();
            log.info("Socket[{}] - '{} 방으로 시그널 전송", client.getSessionId(), room);
            // signal 을 보낸 클라이언트를 제외한, 같은 방에 있는 다른 모든 클라이언트에게
            // signal 이벤트 전달
            client.getNamespace().getRoomOperations(room)
                    .sendEvent("signal", client, data);
        };
    }
}
