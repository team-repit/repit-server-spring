package com.repit.api.controller.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.repit.api.common.ApiResponse;
import com.repit.api.entity.PoseType;
import com.repit.api.entity.Record;
import com.repit.api.service.record.RecordService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    public static class CreateRecordRequest {
        @NotNull(message = "member_id는 필수입니다.")
        @Schema(description = "회원 ID", example = "1")
        private Long member_id;
        @NotNull(message = "pose_type는 필수입니다.")
        @Schema(description = "운동 타입", example = "SQUAT")
        private PoseType pose_type;
        @NotBlank(message = "video_path는 필수입니다.")
        @Schema(description = "영상 파일 경로", example = "https://s3.aws.com/repit/videos/1234.mp4")
        private String video_path;
        @Schema(description = "분석 결과 파일 경로 (선택사항)", example = "/Users/username/Desktop/화면 기록 2025-07-22 오후 3.35.37_report_1.txt")
        private String analysis_path;

        public Long getMember_id() { return member_id; }
        public PoseType getPose_type() { return pose_type; }
        public String getVideo_path() { return video_path; }
        public String getAnalysis_path() { return analysis_path; }
    }

    @PostMapping("/record")
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@Valid @RequestBody CreateRecordRequest body) {
        long id = recordService.create(
            body.getMember_id(),
            body.getPose_type(),
            body.getVideo_path(),
            body.getAnalysis_path()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("RECORD_001", "운동 기록 생성에 성공했습니다.", Map.of("record_id", id)));
    }

    public static class RecordDetailResponse {
        private long record_id;
        private long member_id;
        private PoseType pose_type;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime recorded_at;
        private int duration;
        private int reps;
        private char total_score;
        private String video_path;
        private String analysis_path;
        private LocalDateTime deleted_at;

        public RecordDetailResponse(Record r) {
            this.record_id = r.getRecordId();
            this.member_id = r.getMemberId();
            this.pose_type = r.getPoseType();
            this.recorded_at = r.getRecordedAt();
            this.duration = r.getDuration();
            this.reps = r.getReps();
            this.total_score = r.getTotalScore();
            this.video_path = r.getVideoPath();
            this.analysis_path = r.getAnalysisPath();
            this.deleted_at = r.getDeletedAt();
        }

        public long getRecord_id() { return record_id; }
        public long getMember_id() { return member_id; }
        public PoseType getPose_type() { return pose_type; }
        public LocalDateTime getRecorded_at() { return recorded_at; }
        public int getDuration() { return duration; }
        public int getReps() { return reps; }
        public char getTotal_score() { return total_score; }
        public String getVideo_path() { return video_path; }
        public String getAnalysis_path() { return analysis_path; }
        public LocalDateTime getDeleted_at() { return deleted_at; }
    }

    @GetMapping("/record/{record_id}")
    public ResponseEntity<ApiResponse<RecordDetailResponse>> get(@PathVariable("record_id") long recordId) {
        Record record = recordService.get(recordId);
        return ResponseEntity.ok(ApiResponse.success(
                "RECORD_003",
                "운동 기록 상세 조회에 성공했습니다.",
                new RecordDetailResponse(record)
        ));
    }

    @DeleteMapping("/record/{record_id}/video")
    public ApiResponse<Map<String, Object>> deleteVideo(@PathVariable("record_id") long recordId) {
        recordService.get(recordId);
        return ApiResponse.success("RECORD_005", "영상 삭제 처리에 성공했습니다.", Map.of("record_id", recordId));
    }
}


