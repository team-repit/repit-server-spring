package com.repit.api.service.analysis;

import com.repit.api.common.ApiException;
import com.repit.api.common.ErrorCode;
import com.repit.api.domain.analysis.PoseAnalysis;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 자세 분석 결과 처리 서비스
 */
@Service
public class PoseAnalysisService {
    
    private final AtomicLong analysisIdSeq = new AtomicLong(1000);
    private final Map<Long, PoseAnalysis> analysisStore = new ConcurrentHashMap<>();
    private final Map<String, Long> fileNameToAnalysisId = new ConcurrentHashMap<>();

    /**
     * 자세 분석 결과를 저장합니다.
     * 
     * @param recordId 운동 기록 ID
     * @param analysisText 분석 결과 텍스트
     * @param fileName 분석 파일명
     * @return 생성된 분석 ID
     */
    public long saveAnalysisResult(long recordId, String analysisText, String fileName) {
        // 중복 방지: 동일한 파일명이 이미 존재하는지 확인
        if (fileNameToAnalysisId.containsKey(fileName)) {
            throw new ApiException(ErrorCode.ANALYSIS_4002);
        }

        // 분석 텍스트 파싱
        PoseAnalysis analysis = parseAnalysisText(recordId, analysisText, fileName);
        
        long analysisId = analysisIdSeq.incrementAndGet();
        analysisStore.put(analysisId, analysis);
        fileNameToAnalysisId.put(fileName, analysisId);
        
        return analysisId;
    }

    /**
     * 분석 결과를 조회합니다.
     * 
     * @param analysisId 분석 ID
     * @return 분석 결과
     */
    public PoseAnalysis getAnalysis(long analysisId) {
        PoseAnalysis analysis = analysisStore.get(analysisId);
        if (analysis == null) {
            throw new ApiException(ErrorCode.ANALYSIS_4001);
        }
        return analysis;
    }

    /**
     * 분석 텍스트를 파싱하여 PoseAnalysis 객체로 변환합니다.
     */
    private PoseAnalysis parseAnalysisText(long recordId, String analysisText, String fileName) {
        // 운동 타입 추출 (파일명에서)
        String pose_type = extractPoseTypeFromFileName(fileName);
        
        // 총 유지 시간 추출 (초 단위로 변환)
        int total_duration = (int) extractTotalDuration(analysisText);
        
        // 등급 추출 (char로 변환)
        char total_score = extractGrade(analysisText).charAt(0);
        
        // 구간별 분석 결과 추출
        List<PoseAnalysis.SegmentAnalysis> segments = extractSegments(analysisText);
        
        // ScoreDetail 리스트 생성 (빈 리스트로 시작)
        List<com.repit.api.domain.record.ScoreDetail> score_details = new ArrayList<>();
        
        return new PoseAnalysis(
            recordId,
            pose_type,
            total_duration,
            total_score,
            score_details,
            segments,
            LocalDateTime.now(),
            fileName
        );
    }

    /**
     * 파일명에서 운동 타입을 추출합니다.
     */
    private String extractPoseTypeFromFileName(String fileName) {
        if (fileName.toLowerCase().contains("plank")) {
            return "PLANK";
        } else if (fileName.toLowerCase().contains("squat")) {
            return "SQUAT";
        } else if (fileName.toLowerCase().contains("pushup")) {
            return "PUSHUP";
        }
        return "UNKNOWN";
    }

    /**
     * 총 유지 시간을 추출합니다.
     */
    private double extractTotalDuration(String text) {
        Pattern pattern = Pattern.compile("총.*?유지 시간:\\s*(\\d+\\.?\\d*)초");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0;
    }

    /**
     * 등급을 추출합니다.
     */
    private String extractGrade(String text) {
        Pattern pattern = Pattern.compile("등급\\s*([A-F])");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "UNKNOWN";
    }

    /**
     * 구간별 분석 결과를 추출합니다.
     */
    private List<PoseAnalysis.SegmentAnalysis> extractSegments(String text) {
        List<PoseAnalysis.SegmentAnalysis> segments = new ArrayList<>();
        
        // 구간별 패턴 매칭
        Pattern segmentPattern = Pattern.compile(
            "--- (\\d+)번째 구간.*?유지 시간:\\s*(\\d+\\.?\\d*)초.*?등급\\s*([A-F]) ---",
            Pattern.DOTALL
        );
        Matcher segmentMatcher = segmentPattern.matcher(text);
        
        while (segmentMatcher.find()) {
            int segment_number = Integer.parseInt(segmentMatcher.group(1));
            double duration = Double.parseDouble(segmentMatcher.group(2));
            String grade = segmentMatcher.group(3);
            
            // 오류 상세 정보 추출
            List<PoseAnalysis.ErrorDetail> errors = extractErrors(text, segment_number);
            
            segments.add(new PoseAnalysis.SegmentAnalysis(segment_number, duration, grade, errors));
        }
        
        return segments;
    }

    /**
     * 구간별 오류 상세 정보를 추출합니다.
     */
    private List<PoseAnalysis.ErrorDetail> extractErrors(String text, int segmentNumber) {
        List<PoseAnalysis.ErrorDetail> errors = new ArrayList<>();
        
        // 오류 패턴 매칭
        Pattern errorPattern = Pattern.compile(
            "-\\s*([^:]+):\\s*([^(]+)\\.\\s*\\((\\d+)회 감지\\)"
        );
        Matcher errorMatcher = errorPattern.matcher(text);
        
        while (errorMatcher.find()) {
            String error_type = errorMatcher.group(1).trim();
            String description = errorMatcher.group(2).trim();
            int detection_count = Integer.parseInt(errorMatcher.group(3));
            
            // 오류 레벨 결정
            String level = determineErrorLevel(error_type);
            
            errors.add(new PoseAnalysis.ErrorDetail(error_type, description, detection_count, level));
        }
        
        return errors;
    }

    /**
     * 오류 타입에 따라 레벨을 결정합니다.
     */
    private String determineErrorLevel(String errorType) {
        if (errorType.contains("엉덩이 처짐") || errorType.contains("허리 꺾임")) {
            return "레벨 1: 안전성";
        } else if (errorType.contains("엉덩이 솟음") || errorType.contains("고개 떨굼") || errorType.contains("고개 젖힘")) {
            return "레벨 2: 효과성";
        } else {
            return "레벨 3: 최적화";
        }
    }
}
