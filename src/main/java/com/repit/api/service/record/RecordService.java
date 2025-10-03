package com.repit.api.service.record;

import com.repit.api.common.ApiException;
import com.repit.api.common.ErrorCode;
import com.repit.api.entity.PoseType;
import com.repit.api.entity.Record;
import com.repit.api.repository.RecordRepository;
import com.repit.api.service.analysis.PoseAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecordService {
    
    @Autowired
    private RecordRepository recordRepository;
    
    @Autowired
    private PoseAnalysisService poseAnalysisService;
    
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

    /**
     * 분석 파일을 처리하여 운동 기록을 생성합니다.
     * 
     * @param analysisFilePath 분석 파일 경로
     * @param memberId 회원 ID
     * @return 생성된 운동 기록 ID
     */
    public long createFromAnalysisFile(String analysisFilePath, long memberId) {
        try {
            Path filePath = Paths.get(analysisFilePath);
            if (!Files.exists(filePath)) {
                throw new ApiException(ErrorCode.RECORD_2001); // 파일이 존재하지 않음
            }

            String fileName = filePath.getFileName().toString();
            String analysisText = Files.readString(filePath);
            
            // 파일명에서 운동 타입 추출
            PoseType poseType = extractPoseTypeFromFileName(fileName);
            
            // 비디오 경로 생성 (분석 파일명에서 .txt를 .mp4로 변경)
            String videoPath = analysisFilePath.replace(".txt", ".mp4");
            
            // 1. 운동 기록 생성
            long recordId = create(memberId, poseType, videoPath, analysisFilePath);
            
            // 2. 분석 결과 저장
            long analysisId = poseAnalysisService.saveAnalysisResult(recordId, analysisText, fileName);
            
            // 3. 분석 결과 조회하여 총점 업데이트
            var analysis = poseAnalysisService.getAnalysis(analysisId);
            
            // 4. 운동 기록의 총점 업데이트
            Record record = get(recordId);
            record.setTotalScore(analysis.getTotal_score());
            recordRepository.save(record);
            
            return recordId;
            
        } catch (IOException e) {
            throw new ApiException(ErrorCode.RECORD_2001); // 파일 읽기 실패
        }
    }

    /**
     * 파일명에서 운동 타입을 추출합니다.
     * 파일명 맨 처음에 있는 운동 타입을 우선적으로 확인합니다.
     */
    private PoseType extractPoseTypeFromFileName(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        
        // 파일명 맨 처음에 있는 운동 타입을 우선적으로 확인
        if (lowerFileName.startsWith("lunge")) {
            return PoseType.LUNGE;
        } else if (lowerFileName.startsWith("squat")) {
            return PoseType.SQUAT;
        } else if (lowerFileName.startsWith("plank")) {
            return PoseType.PLANK;
        } else if (lowerFileName.startsWith("pushup") || lowerFileName.startsWith("push_up")) {
            return PoseType.PUSH_UP;
        }
        
        // 파일명 맨 처음에 없으면 전체 파일명에서 검색
        if (lowerFileName.contains("lunge")) {
            return PoseType.LUNGE;
        } else if (lowerFileName.contains("squat")) {
            return PoseType.SQUAT;
        } else if (lowerFileName.contains("plank")) {
            return PoseType.PLANK;
        } else if (lowerFileName.contains("pushup") || lowerFileName.contains("push_up")) {
            return PoseType.PUSH_UP;
        }
        
        return PoseType.SQUAT; // 기본값
    }

}