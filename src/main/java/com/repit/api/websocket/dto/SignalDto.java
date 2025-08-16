package com.repit.api.websocket.dto;

import lombok.Data;

@Data
public class SignalDto {
    // 주소
    private String room;

    // 어떤 종류의 데이터인지 - offer, answer, ice
    private String type;

    // 메시지 내용
    private Object data;
}
