package com.repit.api.config;

import com.repit.api.service.record.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
@EnableIntegration
public class FileIntegrationConfig {

    @Value("${file.watch.directory:/Users/pado/repit/repit-jetson/output}")
    private String watchDirectory;

    @Value("${file.watch.pattern:.*_report_\\d+\\.txt$}")
    private String filePattern;

    @Autowired
    private RecordService recordService;

    private static final String FILE_CHANNEL = "fileChannel";

    // 1. 디렉토리를 주기적으로 스캔하여 새로운 파일을 감지하는 부분
    @Bean
    @InboundChannelAdapter(value = FILE_CHANNEL, poller = @Poller(fixedDelay = "2000")) // 2초마다 폴링
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(watchDirectory));
        
        // 파일 패턴 필터와 중복 처리 방지 필터를 조합
        CompositeFileListFilter<File> compositeFilter = new CompositeFileListFilter<>();
        compositeFilter.addFilter(new RegexPatternFileListFilter(filePattern));
        compositeFilter.addFilter(new AcceptOnceFileListFilter<>());
        sourceReader.setFilter(compositeFilter);
        
        return sourceReader;
    }

    // 2. 새로운 파일이 감지되었을 때 실제 작업을 처리하는 부분
    @Bean
    @ServiceActivator(inputChannel = FILE_CHANNEL)
    public MessageHandler fileMessageHandler() {
        return message -> {
            File newFile = (File) message.getPayload();
            System.out.println("새로운 분석 파일 감지됨: " + newFile.getAbsolutePath());
            
            try {
                // RecordService의 createFromAnalysisFile 메서드 호출
                long recordId = recordService.createFromAnalysisFile(newFile.getAbsolutePath(), 1L);
                System.out.println("분석 파일 처리 완료. Record ID: " + recordId);
            } catch (Exception e) {
                System.err.println("분석 파일 처리 중 오류 발생: " + newFile.getAbsolutePath() + " - " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
