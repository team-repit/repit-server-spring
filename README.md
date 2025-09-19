# 🏃‍♂️ Repit - AI 기반 운동 자세 분석 API 서버

> **Repit**은 AI를 활용한 운동 자세 분석 및 운동 기록 관리 시스템입니다.  
> 사용자의 운동 영상을 분석하여 올바른 자세를 피드백하고, 운동 기록을 체계적으로 관리할 수 있습니다.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Swagger](https://img.shields.io/badge/Swagger-3.0-green.svg)](https://swagger.io/)

## ✨ 주요 기능

### 🎯 운동 자세 분석
- **실시간 자세 분석**: Python AI 모델과 연동하여 운동 자세를 실시간으로 분석
- **다양한 운동 지원**: 스쿼트, 푸시업, 플랭크 등 주요 운동 자세 분석
- **자동 점수 평가**: 운동 자세를 A~F 등급으로 자동 평가
- **상세 피드백**: 신체 부위별 세부 점수 및 개선 사항 제공

### 📊 운동 기록 관리
- **자동 운동 기록**: 운동 시작/종료 시간 자동 기록
- **반복 횟수 계산**: AI를 통한 자동 반복 횟수 계산
- **캘린더 뷰**: 월별/일별 운동 기록 시각화

### 🔄 자동화 시스템
- **파일 감지**: 분석 결과 파일 자동 감지 및 처리
- **실시간 업데이트**: 운동 분석 결과 실시간 데이터베이스 반영
- **중복 방지**: 동일한 영상의 중복 분석 방지

## 🚀 빠른 시작

### 1. 저장소 클론

```bash
git clone https://github.com/your-username/repit-server-spring.git
cd repit-server-spring
```

### 2. 환경 설정

#### .env 파일 생성
```bash
# .env 파일을 프로젝트 루트에 생성
cat > .env << 'EOF'
# 데이터베이스 설정
DB_HOST=localhost
DB_PORT=3306
DB_NAME=repit
DB_USER=root
DB_PASSWORD=your_mysql_password

# 서버 설정
SERVER_PORT=8080

# 파일 감지 설정
FILE_WATCH_DIRECTORY=/Users/username/Desktop
FILE_WATCH_PATTERN=.*_report_\\d+\\.txt$
EOF
```

### 3. 데이터베이스 설정

#### MySQL 설치 및 실행

**macOS (Homebrew):**
```bash
# MySQL 설치
brew install mysql

# MySQL 서비스 시작
brew services start mysql

# MySQL 접속
mysql -u root -p
```

**Ubuntu/Debian:**
```bash
# MySQL 설치
sudo apt update
sudo apt install mysql-server

# MySQL 서비스 시작
sudo systemctl start mysql
sudo systemctl enable mysql

# MySQL 접속
sudo mysql -u root -p
```

#### 데이터베이스 생성
```sql
-- MySQL 접속 후 실행
CREATE DATABASE repit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES; -- 데이터베이스 생성 확인
```

### 4. 애플리케이션 실행

```bash
# 의존성 설치 및 실행
./gradlew bootRun
```

### 5. 서비스 확인

애플리케이션이 정상적으로 시작되면 다음 URL로 접속하여 확인:

- **Health Check**: http://localhost:8080/api/health
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs

## 📚 API 문서

### 🎯 운동 기록 API

#### 운동 기록 생성
```http
POST /api/api/record
Content-Type: application/json

{
  "member_id": 1,
  "pose_type": "SQUAT",
  "video_path": "https://s3.aws.com/repit/videos/1234.mp4",
  "analysis_path": "/Users/username/Desktop/analysis_report_1.txt"
}
```

#### 운동 기록 조회
```http
GET /api/api/record/{record_id}
```

#### 운동 기록 삭제
```http
DELETE /api/api/record/{record_id}/video
```

### 📅 캘린더 API

#### 월별 운동 기록 조회
```http
GET /api/api/calendar/{year}/{month}
```

#### 일별 운동 기록 조회
```http
GET /api/api/calendar/{year}/{month}/{day}
```

### 🤖 자세 분석 API

#### 분석 파일 업로드
```http
POST /api/api/analysis/pose
Content-Type: multipart/form-data

record_id: 1
file: analysis_report.txt
```

## 🛠️ 기술 스택

### Backend
- **Spring Boot 3.3.5** - 메인 프레임워크
- **Spring Data JPA** - 데이터베이스 ORM
- **Spring Web** - REST API 구현
- **Hibernate** - JPA 구현체
- **HikariCP** - 데이터베이스 연결 풀

### Database
- **MySQL 8.0+** - 메인 데이터베이스
- **JPA/Hibernate** - ORM 매핑

### API Documentation
- **Swagger/OpenAPI 3.0** - API 문서화
- **SpringDoc OpenAPI** - Spring Boot 통합

### Development Tools
- **Gradle** - 빌드 도구
- **Spring Boot DevTools** - 개발 편의성
- **DotEnv Java** - 환경변수 관리

## 📁 프로젝트 구조

```
src/main/java/com/repit/api/
├── ApiApplication.java                 # 메인 애플리케이션
├── common/                            # 공통 유틸리티
│   ├── ApiException.java              # 커스텀 예외
│   ├── ApiResponse.java               # API 응답 래퍼
│   ├── ErrorCode.java                 # 에러 코드 관리
│   └── GlobalExceptionHandler.java    # 전역 예외 처리
├── config/                            # 설정 클래스
│   ├── AuthInterceptor.java           # 인증 인터셉터
│   ├── DotEnvConfig.java              # .env 파일 설정
│   ├── SwaggerConfig.java             # Swagger 설정
│   └── WebConfig.java                 # 웹 설정
├── controller/                        # REST 컨트롤러
│   ├── calendar/                      # 캘린더 API
│   ├── record/                        # 운동 기록 API
│   └── analysis/                      # 자세 분석 API
├── entity/                            # JPA 엔티티
│   ├── Record.java                    # 운동 기록 엔티티
│   └── PoseType.java                  # 운동 타입 열거형
├── repository/                        # 데이터 접근 계층
│   └── RecordRepository.java          # 운동 기록 리포지토리
└── service/                           # 비즈니스 로직
    ├── record/                        # 운동 기록 서비스
    ├── calendar/                      # 캘린더 서비스
    ├── analysis/                      # 자세 분석 서비스
    └── FileWatcherService.java        # 파일 감지 서비스
```

## 🔧 환경변수 설정

| 변수명 | 설명 | 기본값 | 필수 |
|--------|------|--------|------|
| `DB_HOST` | 데이터베이스 호스트 | localhost | ✅ |
| `DB_PORT` | 데이터베이스 포트 | 3306 | ✅ |
| `DB_NAME` | 데이터베이스 이름 | repit | ✅ |
| `DB_USER` | 데이터베이스 사용자 | root | ✅ |
| `DB_PASSWORD` | 데이터베이스 비밀번호 | - | ✅ |
| `SERVER_PORT` | 서버 포트 | 8080 | ❌ |
| `FILE_WATCH_DIRECTORY` | 파일 감지 디렉토리 | /Users/username/Desktop | ❌ |
| `FILE_WATCH_PATTERN` | 파일 감지 패턴 | .*_report_\\d+\\.txt$ | ❌ |

---
