package com.repit.api.domain.record;

/**
 * 점수 상세 도메인
 */
public class ScoreDetail {
    private final long score_id;
    private final long record_id; // FK to record table
    private final String body_part; // 신체부위명
    private final char detail_score; // 부위별 세부 점수

    public ScoreDetail(long score_id, long record_id, String body_part, char detail_score) {
        this.score_id = score_id;
        this.record_id = record_id;
        this.body_part = body_part;
        this.detail_score = detail_score;
    }

    // Getters
    public long getScore_id() { return score_id; }
    public long getRecord_id() { return record_id; }
    public String getBody_part() { return body_part; }
    public char getDetail_score() { return detail_score; }
}
