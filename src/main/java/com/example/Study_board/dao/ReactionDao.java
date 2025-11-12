package com.example.Study_board.dao;

import com.example.Study_board.dto.ReactionCreateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j

public class ReactionDao {
    JdbcTemplate jdbc;
    // 추천 신고 입력 기능
    public Long recordReaction(ReactionCreateReq req) {
    @Language("SQL")
    String sql = """
    INSERT INTO REACTION (REACTION_ID, MEMBER_ID, TARGET_TYPE, TARGET_ID, ACTION) 
    VALUES(SEQ_STUDY_REACTION.nextval, ?, ?, ?, ?);
    """;
    jdbc.update(sql, req.getMember_id(), req.getTarget_type(), req.getTarget_id(), req.getAction());
    return jdbc.queryForObject("SELECT SEQ_STUDY_REACTION.CURRVAL FROM dual", Long.class);
    }
}
