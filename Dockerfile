# 1. Java 21 기반 이미지 사용 (Amazon Corretto)
FROM amazoncorretto:21

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (아래 gradle 빌드 후 생성되는 JAR을 기준으로)
COPY build/libs/*.jar app.jar

# 4. 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# 5. 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# 6. JAR 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]