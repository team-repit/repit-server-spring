package com.repit.api.common;

import org.springframework.http.HttpStatus;

/**
 * 애플리케이션 전체에서 사용하는 에러 코드를 정의합니다.
 */
public enum ErrorCode {
    
    // ===== 공통 에러 (1000-1999) =====
    COMMON_1001(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON_1002(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    COMMON_1003(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터입니다."),
    COMMON_1004(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    COMMON_1005(HttpStatus.SERVICE_UNAVAILABLE, "서비스가 일시적으로 사용할 수 없습니다."),
    
    // ===== 운동 기록 관련 에러 (2000-2999) =====
    RECORD_2001(HttpStatus.NOT_FOUND, "해당 운동 기록을 찾을 수 없습니다."),
    RECORD_2002(HttpStatus.CONFLICT, "이미 존재하는 영상입니다. 중복 생성할 수 없습니다."),
    RECORD_2003(HttpStatus.BAD_REQUEST, "유효하지 않은 운동 타입입니다."),
    RECORD_2004(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다."),
    RECORD_2005(HttpStatus.BAD_REQUEST, "운동 기록 생성에 실패했습니다."),
    RECORD_2006(HttpStatus.BAD_REQUEST, "운동 기록 수정에 실패했습니다."),
    RECORD_2007(HttpStatus.BAD_REQUEST, "운동 기록 삭제에 실패했습니다."),
    
    // ===== 캘린더 관련 에러 (3000-3999) =====
    CALENDAR_3001(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜입니다."),
    CALENDAR_3002(HttpStatus.BAD_REQUEST, "유효하지 않은 월입니다."),
    CALENDAR_3003(HttpStatus.BAD_REQUEST, "유효하지 않은 년도입니다."),
    CALENDAR_3004(HttpStatus.NOT_FOUND, "해당 날짜의 운동 기록을 찾을 수 없습니다."),
    CALENDAR_101(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),
    
    // ===== 자세 분석 관련 에러 (4000-4999) =====
    ANALYSIS_4001(HttpStatus.NOT_FOUND, "해당 분석 결과를 찾을 수 없습니다."),
    ANALYSIS_4002(HttpStatus.CONFLICT, "이미 처리된 분석 파일입니다."),
    ANALYSIS_4003(HttpStatus.BAD_REQUEST, "분석 파일을 읽는 중 오류가 발생했습니다."),
    ANALYSIS_4004(HttpStatus.BAD_REQUEST, "유효하지 않은 분석 파일 형식입니다."),
    ANALYSIS_4005(HttpStatus.BAD_REQUEST, "분석 결과 파싱에 실패했습니다."),
    ANALYSIS_4006(HttpStatus.BAD_REQUEST, "분석 파일이 비어있습니다."),
    ANALYSIS_4007(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    
    // ===== 회원 관련 에러 (5000-5999) =====
    MEMBER_5001(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MEMBER_5002(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    MEMBER_5003(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 형식입니다."),
    MEMBER_5004(HttpStatus.BAD_REQUEST, "유효하지 않은 회원 정보입니다."),
    MEMBER_5005(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    MEMBER_5006(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    
    // ===== 영상 관련 에러 (6000-6999) =====
    VIDEO_6001(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다."),
    VIDEO_6002(HttpStatus.BAD_REQUEST, "영상 파일을 처리할 수 없습니다."),
    VIDEO_6003(HttpStatus.BAD_REQUEST, "영상 메타데이터를 읽을 수 없습니다."),
    VIDEO_6004(HttpStatus.BAD_REQUEST, "지원하지 않는 영상 형식입니다."),
    VIDEO_6005(HttpStatus.REQUEST_TIMEOUT, "영상 처리 시간이 초과되었습니다."),
    
    // ===== 파일 업로드 관련 에러 (7000-7999) =====
    FILE_7001(HttpStatus.BAD_REQUEST, "파일이 제공되지 않았습니다."),
    FILE_7002(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다."),
    FILE_7003(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    FILE_7004(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    FILE_7005(HttpStatus.BAD_REQUEST, "파일을 읽을 수 없습니다."),
    FILE_7006(HttpStatus.BAD_REQUEST, "파일명이 유효하지 않습니다."),
    
    // ===== 데이터베이스 관련 에러 (8000-8999) =====
    DATABASE_8001(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다."),
    DATABASE_8002(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장에 실패했습니다."),
    DATABASE_8003(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 조회에 실패했습니다."),
    DATABASE_8004(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 수정에 실패했습니다."),
    DATABASE_8005(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 삭제에 실패했습니다."),
    DATABASE_8006(HttpStatus.CONFLICT, "데이터 중복 오류가 발생했습니다."),
    
    // ===== 외부 API 관련 에러 (9000-9999) =====
    EXTERNAL_9001(HttpStatus.BAD_GATEWAY, "외부 서비스와의 통신에 실패했습니다."),
    EXTERNAL_9002(HttpStatus.SERVICE_UNAVAILABLE, "외부 서비스가 사용할 수 없습니다."),
    EXTERNAL_9003(HttpStatus.REQUEST_TIMEOUT, "외부 서비스 응답 시간이 초과되었습니다."),
    EXTERNAL_9004(HttpStatus.BAD_REQUEST, "외부 서비스 요청이 잘못되었습니다."),
    EXTERNAL_9005(HttpStatus.UNAUTHORIZED, "외부 서비스 인증에 실패했습니다.");
    
    private final HttpStatus httpStatus;
    private final String message;
    
    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getCode() {
        return httpStatus.value();
    }
}
