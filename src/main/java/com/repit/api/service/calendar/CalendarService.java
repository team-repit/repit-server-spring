package com.repit.api.service.calendar;

import com.repit.api.entity.Record;
import com.repit.api.service.record.RecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 캘린더 서비스
 */
@Service
public class CalendarService {
    
    private final RecordService recordService;
    
    public CalendarService(RecordService recordService) {
        this.recordService = recordService;
    }
    
    /**
     * 특정 월에 운동 기록이 있는 날짜들을 반환합니다.
     * 
     * @param year 년도
     * @param month 월
     * @return 운동 기록이 있는 날짜들의 리스트
     */
    public List<Integer> getDaysWithRecords(int year, int month) {
        return recordService.getDaysWithRecords(year, month).stream()
                .map(LocalDate::getDayOfMonth)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 날짜의 운동 기록들을 반환합니다.
     * 
     * @param year 년도
     * @param month 월
     * @param day 일
     * @return 해당 날짜의 운동 기록 리스트
     */
    public List<Record> getRecordsByDate(int year, int month, int day) {
        return recordService.getRecordsByDate(year, month, day);
    }
}
