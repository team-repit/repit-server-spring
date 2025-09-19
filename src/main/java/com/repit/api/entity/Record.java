package com.repit.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "records", 
       uniqueConstraints = @UniqueConstraint(
           name = "uk_video_path_active", 
           columnNames = "video_path"
       ))
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pose_type", nullable = false)
    private PoseType poseType;
    
    @Column(name = "duration", nullable = false)
    private Integer duration;
    
    @Column(name = "reps", nullable = false)
    private Integer reps;
    
    @Column(name = "total_score", nullable = false)
    private Character totalScore;
    
    @Column(name = "video_path", nullable = false)
    private String videoPath;
    
    @Column(name = "analysis_path")
    private String analysisPath;
    
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    // 기본 생성자
    public Record() {}
    
    // 생성자
    public Record(Long memberId, PoseType poseType, Integer duration, Integer reps, 
                  Character totalScore, String videoPath, String analysisPath, 
                  LocalDateTime recordedAt, LocalDateTime deletedAt) {
        this.memberId = memberId;
        this.poseType = poseType;
        this.duration = duration;
        this.reps = reps;
        this.totalScore = totalScore;
        this.videoPath = videoPath;
        this.analysisPath = analysisPath;
        this.recordedAt = recordedAt;
        this.deletedAt = deletedAt;
    }
    
    // Getters and Setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    
    public PoseType getPoseType() { return poseType; }
    public void setPoseType(PoseType poseType) { this.poseType = poseType; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }
    
    public Character getTotalScore() { return totalScore; }
    public void setTotalScore(Character totalScore) { this.totalScore = totalScore; }
    
    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }
    
    public String getAnalysisPath() { return analysisPath; }
    public void setAnalysisPath(String analysisPath) { this.analysisPath = analysisPath; }
    
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    
    public LocalDate getRecordDate() {
        return recordedAt != null ? recordedAt.toLocalDate() : null;
    }
}
