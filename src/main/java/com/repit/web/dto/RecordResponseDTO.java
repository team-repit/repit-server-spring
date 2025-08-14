package com.repit.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.repit.domain.Record;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponseDTO {
    private Long recordId;
    private Long memberId; // Include member ID for context
    private String poseType;
    private Integer duration;
    private Integer reps;
    private Character totalScore;
    private String videoName;
    private LocalDateTime recordedAt;
    private LocalDateTime deletedAt;

    public RecordResponseDTO(Record record) {
        this.recordId = record.getRecordId();
        this.memberId = record.getMember().getMemberId(); // Get member ID from associated member
        this.poseType = record.getPoseType();
        this.duration = record.getDuration();
        this.reps = record.getReps();
        this.totalScore = record.getTotalScore();
        this.videoName = record.getVideoName();
        this.recordedAt = record.getRecordedAt();
        this.deletedAt = record.getDeletedAt();
    }
}