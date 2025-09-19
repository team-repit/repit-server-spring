package com.repit.api.service.record;

import com.repit.api.common.ApiException;
import com.repit.api.common.ErrorCode;
import com.repit.api.entity.PoseType;
import com.repit.api.entity.Record;
import com.repit.api.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecordService {
    
    @Autowired
    private RecordRepository recordRepository;
    
    public long create(long memberId, PoseType poseType, String videoPath, String analysisPath) {
        // 중복 생성 방지: 동일한 video_path가 이미 존재하는지 확인
        if (recordRepository.existsByVideoPathAndDeletedAtIsNull(videoPath)) {
            throw new ApiException(ErrorCode.RECORD_2002);
        }

        // 자동 계산 또는 기본값 설정
        int duration = calculateDurationFromVideo(videoPath);
        int reps = calculateRepsFromPoseType(poseType);
        char totalScore = 'C'; // 기본값: 분석 전까지는 C등급

        Record record = new Record(memberId, poseType, duration, reps, totalScore, 
                                 videoPath, analysisPath, LocalDateTime.now(), null);
        
        try {
            Record savedRecord = recordRepository.save(record);
            return savedRecord.getRecordId();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ApiException(ErrorCode.RECORD_2002);
        }
    }
    
    public Record get(long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RECORD_2001));
    }
    
    public void delete(long id) {
        Record record = get(id);
        record.setDeletedAt(LocalDateTime.now());
        recordRepository.save(record);
    }
    
    public List<Record> getAllRecords() {
        return recordRepository.findAllActiveRecords();
    }
    
    public void updateAnalysisPath(long recordId, String analysisPath) {
        Record record = get(recordId);
        record.setAnalysisPath(analysisPath);
        recordRepository.save(record);
    }
    
    public List<Record> getRecordsByDate(int year, int month, int day) {
        java.time.LocalDate targetDate = java.time.LocalDate.of(year, month, day);
        var start = targetDate.atStartOfDay();
        var end = targetDate.plusDays(1).atStartOfDay();
        return recordRepository.findByRecordedAtBetween(start, end);
    }
    
    public List<java.time.LocalDate> getDaysWithRecords(int year, int month) {
        var start = java.time.LocalDate.of(year, month, 1).atStartOfDay();
        var end = start.plusMonths(1);
        return recordRepository.findByRecordedAtBetweenMonth(start, end).stream()
                .map(Record::getRecordedAt)
                .map(java.time.LocalDateTime::toLocalDate)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
    }
    
    private int calculateDurationFromVideo(String videoPath) {
        // 실제 구현에서는 비디오 파일의 메타데이터를 읽어서 계산
        // 현재는 기본값 반환
        return 60; // 1분 기본값
    }
    
    private int calculateRepsFromPoseType(PoseType poseType) {
        switch (poseType) {
            case SQUAT:
                return 10;
            case PUSH_UP:
                return 15;
            case PLANK:
                return 5;
            default:
                return 10;
        }
    }

    public void updateVideoPath(long recordId, String videoPath) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ApiException(ErrorCode.RECORD_2001));
        record.setVideoPath(videoPath);
        recordRepository.save(record);
    }
}