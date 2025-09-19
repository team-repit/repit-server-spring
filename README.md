# Api Server for Repit

## 환경 설정

### 환경변수 설정

애플리케이션 실행 전에 다음 환경변수들을 설정해주세요:

```bash
# 데이터베이스 설정
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=repit
export DB_USER=root
export DB_PASSWORD=your_password

# 서버 설정
export SERVER_PORT=8080

# 파일 감지 설정 (선택사항)
export FILE_WATCH_DIRECTORY=/Users/username/Desktop
export FILE_WATCH_PATTERN=.*_report_\\d+\\.txt$
```

### 데이터베이스 설정

#### 1. MySQL 설치 및 실행

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

**Windows:**
- MySQL Community Server 다운로드 및 설치
- MySQL Workbench 또는 명령 프롬프트에서 접속

#### 2. 데이터베이스 생성

```sql
-- MySQL 접속 후 실행
CREATE DATABASE repit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES; -- 데이터베이스 생성 확인
```

#### 3. 환경변수 설정 및 실행

```bash
# 환경변수 설정
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=repit
export DB_USER=root
export DB_PASSWORD=your_mysql_password

# 애플리케이션 실행
./gradlew bootRun
```

#### 4. 연결 테스트

애플리케이션이 정상적으로 시작되면 다음 URL로 접속하여 확인:
- Health Check: http://localhost:8080/api/health
- Swagger UI: http://localhost:8080/api/swagger-ui.html

### API 문서

- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/api-docs