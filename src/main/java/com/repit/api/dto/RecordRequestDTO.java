package com.repit.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecordRequestDTO {
    private Long memberId; // To link to the member
    private String poseType;
    private Integer duration;
    private Integer reps;
    private Character totalScore;
    private String videoName;
}