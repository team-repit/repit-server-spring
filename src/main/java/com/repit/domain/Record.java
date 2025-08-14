package com.repit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "member") // Exclude member to prevent infinite loop in toString
@Table(name = "record") // Assuming table name is 'record'
@EntityListeners(AuditingEntityListener.class) // For @CreatedDate
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY) // Many records to one member
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "pose_type", length = 30, nullable = false)
    private String poseType;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "reps", nullable = false)
    private Integer reps;

    @Column(name = "total_score", length = 1, nullable = false)
    private Character totalScore; // Using Character for CHAR(1)

    @Column(name = "video_name", length = 255)
    private String videoName;

    @CreatedDate
    @Column(name = "recorded_at", updatable = false) // Not updatable after creation
    private LocalDateTime recordedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // Nullable for soft delete

    // Constructor for creating a new record
    public Record(Member member, String poseType, Integer duration, Integer reps, Character totalScore, String videoName) {
        this.member = member;
        this.poseType = poseType;
        this.duration = duration;
        this.reps = reps;
        this.totalScore = totalScore;
        this.videoName = videoName;
    }
}