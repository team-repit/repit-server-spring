package com.repit.api.domain.analysis;

import com.repit.api.domain.record.ScoreDetail;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 자세 분석 결과 도메인
 * ERD의 record와 score_detail 테이블에 매핑되는 분석 결과
 */
public class PoseAnalysis {
    private final long record_id;
    private final String pose_type;
    private final int total_duration; // 초 단위로 변환
    private final char total_score; // ERD의 total_score에 매핑
    private final List<ScoreDetail> score_details; // ERD의 score_detail 테이블에 매핑
    private final List<SegmentAnalysis> segments; // 분석 상세 정보
    private final LocalDateTime analyzed_at;
    private final String file_name;

    public PoseAnalysis(long record_id, String pose_type, int total_duration, 
                       char total_score, List<ScoreDetail> score_details, List<SegmentAnalysis> segments, 
                       LocalDateTime analyzed_at, String file_name) {
        this.record_id = record_id;
        this.pose_type = pose_type;
        this.total_duration = total_duration;
        this.total_score = total_score;
        this.score_details = score_details;
        this.segments = segments;
        this.analyzed_at = analyzed_at;
        this.file_name = file_name;
    }

    // Getters
    public long getRecord_id() { return record_id; }
    public String getPose_type() { return pose_type; }
    public int getTotal_duration() { return total_duration; }
    public char getTotal_score() { return total_score; }
    public List<ScoreDetail> getScore_details() { return score_details; }
    public List<SegmentAnalysis> getSegments() { return segments; }
    public LocalDateTime getAnalyzed_at() { return analyzed_at; }
    public String getFile_name() { return file_name; }

    /**
     * 구간별 분석 결과
     */
    public static class SegmentAnalysis {
        private final int segment_number;
        private final double duration;
        private final String grade;
        private final List<ErrorDetail> errors;

        public SegmentAnalysis(int segment_number, double duration, String grade, List<ErrorDetail> errors) {
            this.segment_number = segment_number;
            this.duration = duration;
            this.grade = grade;
            this.errors = errors;
        }

        // Getters
        public int getSegment_number() { return segment_number; }
        public double getDuration() { return duration; }
        public String getGrade() { return grade; }
        public List<ErrorDetail> getErrors() { return errors; }
    }

    /**
     * 오류 상세 정보
     */
    public static class ErrorDetail {
        private final String error_type;
        private final String description;
        private final int detection_count;
        private final String level;

        public ErrorDetail(String error_type, String description, int detection_count, String level) {
            this.error_type = error_type;
            this.description = description;
            this.detection_count = detection_count;
            this.level = level;
        }

        // Getters
        public String getError_type() { return error_type; }
        public String getDescription() { return description; }
        public int getDetection_count() { return detection_count; }
        public String getLevel() { return level; }
    }
}
