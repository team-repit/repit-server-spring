package com.repit.api.service;

import com.repit.api.service.analysis.PoseAnalysisService;
import com.repit.api.service.record.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 파일 감지 서비스 - 특정 패턴의 txt 파일이 생성되면 자동으로 분석 결과를 POST
 */
@Service
public class FileWatcherService implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(FileWatcherService.class);
    
    @Value("${file.watch.directory:}")
    private String watchDirectory;
    
    @Value("${file.watch.pattern:.*_report_\\d+\\.txt$}")
    private String filePattern;
    
    @Value("${file.watch.enabled:false}")
    private boolean watchEnabled;
    
    private final PoseAnalysisService poseAnalysisService;
    private final RecordService recordService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "file-watcher");
        t.setDaemon(true);
        return t;
    });
    private WatchService watchService;
    
    @Autowired
    public FileWatcherService(PoseAnalysisService poseAnalysisService, RecordService recordService) {
        this.poseAnalysisService = poseAnalysisService;
        this.recordService = recordService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        startWatching();
    }
    
    private void startWatching() {
        if (!watchEnabled || watchDirectory == null || watchDirectory.isBlank()) {
            logger.info("파일 감지 비활성화됨 (file.watch.enabled=false)");
            return;
        }
        executorService.submit(() -> {
            try {
                watchDirectory();
            } catch (Exception e) {
                logger.error("파일 감지 서비스 오류", e);
            }
        });
    }
    
    private void watchDirectory() throws IOException, InterruptedException {
        Path path = Paths.get(watchDirectory);
        
        if (!Files.exists(path)) {
            logger.warn("감시할 디렉토리가 존재하지 않습니다: {}", watchDirectory);
            return;
        }
        
        watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        
        logger.info("파일 감지 시작: {}", watchDirectory);
        logger.info("파일 패턴: {}", filePattern);
        
        Pattern pattern = Pattern.compile(filePattern);
        
        while (true) {
            WatchKey key = watchService.take();
            
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    
                    if (isTargetFile(filename.toString(), pattern)) {
                        logger.info("새로운 분석 파일 감지: {}", filename);
                        processFile(path.resolve(filename));
                    }
                }
            }
            
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
    
    private boolean isTargetFile(String filename, Pattern pattern) {
        Matcher matcher = pattern.matcher(filename);
        return matcher.matches();
    }
    
    private void processFile(Path filePath) {
        try {
            // 파일이 완전히 쓰여질 때까지 잠시 대기
            Thread.sleep(1000);
            
            if (!Files.exists(filePath)) {
                logger.warn("파일이 존재하지 않습니다: {}", filePath);
                return;
            }
            
            // 파일 내용 읽기
            String content = Files.readString(filePath, java.nio.charset.StandardCharsets.UTF_8);
            
            // 파일명에서 record_id 추출 (예: "화면 기록 2025-07-22 오후 3.35.37_report_1.txt")
            Long recordId = extractRecordIdFromFilename(filePath.getFileName().toString());
            
            if (recordId == null) {
                logger.warn("파일명에서 record_id를 추출할 수 없습니다: {}", filePath.getFileName());
                return;
            }
            
            // 분석 결과 저장
            long analysisId = poseAnalysisService.saveAnalysisResult(recordId, content, filePath.getFileName().toString());
            
            // Record의 analysis_path 업데이트
            recordService.updateAnalysisPath(recordId, filePath.toString());
            
            logger.info("분석 결과 자동 저장 완료 - Record ID: {}, Analysis ID: {}, File Path: {}", 
                       recordId, analysisId, filePath.toString());
            
        } catch (Exception e) {
            logger.error("파일 처리 중 오류 발생: {}", filePath, e);
        }
    }
    
    private Long extractRecordIdFromFilename(String filename) {
        try {
            // 파일명에서 숫자 추출 (예: "report_1.txt" -> 1)
            Pattern pattern = Pattern.compile("report_(\\d+)\\.txt");
            Matcher matcher = pattern.matcher(filename);
            
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }
        } catch (Exception e) {
            logger.warn("파일명에서 record_id 추출 실패: {}", filename, e);
        }
        
        return null;
    }

    @jakarta.annotation.PreDestroy
    void shutdown() {
        try {
            if (watchService != null) watchService.close();
        } catch (IOException ignore) { }
        executorService.shutdownNow();
    }
}
