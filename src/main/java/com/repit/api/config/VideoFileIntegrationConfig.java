package com.repit.api.config;

import com.repit.api.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class VideoFileIntegrationConfig {

    @Value("${file.watch.directory:/Users/pado/repit/repit-jetson/output}")
    private String watchDirectory;

    @Autowired
    private RecordService recordService;

    private static final String VIDEO_FILE_CHANNEL = "videoFileChannel";

    // MP4 파일 패턴: lunge_realtime_tts_analysis_*.mp4, squat_realtime_tts_analysis_*.mp4 등
    private static final String VIDEO_FILE_PATTERN = ".*_realtime_tts_analysis_.*\\.mp4$";

    // 1. MP4 파일을 감지하는 부분
    @Bean
    @InboundChannelAdapter(value = VIDEO_FILE_CHANNEL, poller = @Poller(fixedDelay = "2000")) // 2초마다 폴링
    public MessageSource<File> videoFileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(watchDirectory));
        
        // MP4 파일 패턴 필터 설정
        sourceReader.setFilter(new RegexPatternFileListFilter(VIDEO_FILE_PATTERN));
        
        return sourceReader;
    }

    // 2. 새로운 MP4 파일이 감지되었을 때 처리하는 부분
    @Bean
    @ServiceActivator(inputChannel = VIDEO_FILE_CHANNEL)
    public MessageHandler videoFileMessageHandler() {
        return message -> {
            File newFile = (File) message.getPayload();
            System.out.println("새로운 비디오 파일 감지됨: " + newFile.getAbsolutePath());
            
            try {
                // RecordService의 processVideoFile 메서드 호출
                recordService.processVideoFile(newFile.getAbsolutePath());
                System.out.println("비디오 파일 처리 완료: " + newFile.getName());
            } catch (Exception e) {
                System.err.println("비디오 파일 처리 중 오류 발생: " + newFile.getAbsolutePath() + " - " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
