package com.repit.api.controller.analysis;

import com.repit.api.common.ApiResponse;
import com.repit.api.common.ErrorCode;
import com.repit.api.domain.analysis.PoseAnalysis;
import com.repit.api.service.analysis.PoseAnalysisService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 자세 분석 결과 API 컨트롤러
 */
@RestController
@RequestMapping("/analysis")
public class PoseAnalysisController {

    private final PoseAnalysisService poseAnalysisService;

    public PoseAnalysisController(PoseAnalysisService poseAnalysisService) {
        this.poseAnalysisService = poseAnalysisService;
    }


    /**
     * 자세 분석 결과를 조회합니다.
     * 
     * @param analysisId 분석 ID
     * @return 분석 결과
     */
    @GetMapping("/pose/{analysis_id}")
    public ResponseEntity<ApiResponse<PoseAnalysis>> getAnalysis(
            @PathVariable("analysis_id") long analysisId) {
        
        PoseAnalysis analysis = poseAnalysisService.getAnalysis(analysisId);
        
        return ResponseEntity.ok(ApiResponse.success(
            "ANALYSIS_002",
            "자세 분석 결과 조회에 성공했습니다.",
            analysis
        ));
    }

}
