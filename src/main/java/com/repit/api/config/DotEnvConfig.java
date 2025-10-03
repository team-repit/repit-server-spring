package com.repit.api.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile({"local", "dev"})
public class DotEnvConfig {

    @PostConstruct
    public void loadDotEnv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        
        // .env 파일의 변수들을 시스템 환경변수로 설정
        // 시스템 프로퍼티와 OS 환경변수가 모두 없을 때만 .env 값 사용
        dotenv.entries().forEach(entry -> {
            final String key = entry.getKey();
            if (System.getProperty(key) == null && System.getenv(key) == null) {
                System.setProperty(key, entry.getValue());
            }
        });
    }
}
