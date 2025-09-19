package com.repit.api.repository;

import com.repit.api.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    
    // 특정 날짜의 기록 조회
    @Query("SELECT r FROM Record r WHERE DATE(r.recordedAt) = :date AND r.deletedAt IS NULL")
    List<Record> findByRecordDate(@Param("date") LocalDate date);
    
    // 특정 월의 기록이 있는 날짜들 조회
    @Query("SELECT DISTINCT r.recordedAt FROM Record r WHERE YEAR(r.recordedAt) = :year AND MONTH(r.recordedAt) = :month AND r.deletedAt IS NULL")
    List<LocalDateTime> findDistinctRecordDatesByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    // 비삭제된 모든 기록 조회
    @Query("SELECT r FROM Record r WHERE r.deletedAt IS NULL")
    List<Record> findAllActiveRecords();
    
    // 비디오 경로로 중복 확인
    boolean existsByVideoPathAndDeletedAtIsNull(String videoPath);
}
