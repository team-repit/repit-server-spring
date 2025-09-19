package com.repit.api.domain.record;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Record {
    private final long record_id;
    private final long member_id; // FK to member table
    private final PoseType pose_type;
    private final int duration; // 운동 시간(초단위) - ERD에 맞게 INTEGER
    private final int reps; // 반복 횟수 카운트
    private final char total_score; // 자세 점수 - ERD에 맞게 CHAR(1)
    private final String video_path; // 영상 파일 경로 - ERD에 맞게 video_path
    private final String analysis_path; // 분석 결과 파일 경로
    private final LocalDateTime recorded_at;
    private final LocalDateTime deleted_at;

    public Record(long record_id, long member_id, PoseType pose_type, int duration, 
                 int reps, char total_score, String video_path, String analysis_path, 
                 LocalDateTime recorded_at, LocalDateTime deleted_at) {
        this.record_id = record_id;
        this.member_id = member_id;
        this.pose_type = pose_type;
        this.duration = duration;
        this.reps = reps;
        this.total_score = total_score;
        this.video_path = video_path;
        this.analysis_path = analysis_path;
        this.recorded_at = recorded_at;
        this.deleted_at = deleted_at;
    }

    public long getRecord_id() { return record_id; }
    public long getMember_id() { return member_id; }
    public PoseType getPose_type() { return pose_type; }
    public int getDuration() { return duration; }
    public int getReps() { return reps; }
    public char getTotal_score() { return total_score; }
    public String getVideo_path() { return video_path; }
    public String getAnalysis_path() { return analysis_path; }
    public LocalDateTime getRecorded_at() { return recorded_at; }
    public LocalDateTime getDeleted_at() { return deleted_at; }

    public LocalDate getRecordDate() {
        return recorded_at.toLocalDate();
    }
}


