package com.repit.api.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 영상 분석을 통한 운동 시간 계산 유틸리티
 */
@Component
public class VideoAnalyzer {
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    /**
     * 영상 URL에서 메타데이터를 분석하여 운동 시간을 계산합니다.
     * 실제 구현에서는 FFmpeg나 다른 영상 분석 라이브러리를 사용할 수 있습니다.
     * 
     * @param videoUrl 영상 URL
     * @return 계산된 운동 시간 (HH:mm:ss 형식)
     */
    public String calculateDurationFromVideo(String videoUrl) {
        // TODO: 실제 영상 분석 로직 구현
        // 현재는 예시로 기본값을 반환합니다.
        // 실제로는 FFmpeg를 사용하여 영상의 실제 길이를 분석해야 합니다.
        
        // 예시: URL에서 영상 ID를 추출하여 분석하는 로직
        if (videoUrl.contains("repit/videos/")) {
            // 실제 구현에서는 영상 파일을 다운로드하거나 스트리밍하여
            // 메타데이터를 분석하여 실제 길이를 계산합니다.
            return "00:01:30"; // 기본값
        }
        
        return "00:00:00";
    }
    
    /**
     * 시간 문자열을 초 단위로 변환합니다.
     * 
     * @param timeString HH:mm:ss 형식의 시간 문자열
     * @return 초 단위 시간
     */
    public long timeStringToSeconds(String timeString) {
        if (timeString == null) {
            throw new IllegalArgumentException("시간 문자열은 null일 수 없습니다");
        }
        try {
            String[] parts = timeString.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("잘못된 시간 형식입니다: " + timeString);
            }
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            
            if (minutes >= 60 || seconds >= 60) {
                throw new IllegalArgumentException("잘못된 시간 형식입니다: " + timeString);
            }
            
            return hours * 3600 + minutes * 60 + seconds;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 시간 형식입니다: " + timeString);
        }
    }
    
    /**
     * 초 단위 시간을 HH:mm:ss 형식 문자열로 변환합니다.
     * 
     * @param seconds 초 단위 시간
     * @return HH:mm:ss 형식의 시간 문자열
     */
    public String secondsToTimeString(long seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("초 단위 시간은 음수일 수 없습니다: " + seconds);
        }
        Duration duration = Duration.ofSeconds(seconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long secs = duration.toSecondsPart();
        
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    
    /**
     * 영상 URL이 유효한지 검증합니다.
     * 
     * @param videoUrl 영상 URL
     * @return 유효한 URL인지 여부
     */
    public boolean isValidVideoUrl(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().isEmpty()) {
            return false;
        }
        
        // 기본적인 URL 형식 검증
        return videoUrl.startsWith("http://") || videoUrl.startsWith("https://");
    }
}
