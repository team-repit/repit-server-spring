package com.repit.api.controller.calendar;

import com.repit.api.common.ApiResponse;
import com.repit.api.common.ErrorCode;
import com.repit.api.entity.Record;
import com.repit.api.service.calendar.CalendarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/{year}/{month}")
    public ApiResponse<Map<String, Object>> getMonthly(@PathVariable int year, @PathVariable int month) {
        List<Integer> daysWithRecords = calendarService.getDaysWithRecords(year, month);
        
        return ApiResponse.success("CAL_001", "월별 기록 조회에 성공했습니다.",
                Map.of("year", year, "month", month, "days", daysWithRecords));
    }

    @GetMapping("/{year}/{month}/{day}")
    public ApiResponse<Map<String, Object>> getDaily(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        // 날짜 유효성 검증
        try {
            java.time.LocalDate.of(year, month, day);
        } catch (Exception e) {
            return ApiResponse.failure(ErrorCode.CALENDAR_101.name(), ErrorCode.CALENDAR_101.getMessage());
        }
        
        List<Record> records = calendarService.getRecordsByDate(year, month, day);
        
        // Record를 요청된 형태로 변환
        List<Map<String, Object>> recordResponses = records.stream()
                .map(record -> {
                    Map<String, Object> recordMap = new java.util.HashMap<>();
                    recordMap.put("recordId", record.getRecordId());
                    recordMap.put("exercise_type", record.getPoseType().name());
                    recordMap.put("start_time", record.getRecordedAt());
                    recordMap.put("video_path", record.getVideoPath());
                    recordMap.put("analysis_path", record.getAnalysisPath());
                    return recordMap;
                })
                .collect(Collectors.toList());
        
        // 날짜 형식 변환 (YYYY-MM-DD)
        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        
        return ApiResponse.success("CALENDAR_002", "일별 운동 기록 조회에 성공했습니다.",
                Map.of("date", dateString, "records", recordResponses));
    }
}


