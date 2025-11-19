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
    private final JdbcTemplate jdbc;
    // 특정 회원의 기존 리액션 체크
    public Long recordReaction(ReactionCreateReq req) {

        // 1) 먼저 중복 확인
        String checkSql = """
        SELECT COUNT(*) 
        FROM REACTION
        WHERE MEMBER_ID = ?
          AND TARGET_TYPE = ?
          AND TARGET_ID = ?
    """;

        Integer count = jdbc.queryForObject(checkSql, Integer.class,
                req.getMember_id(),
                req.getTarget_type(),
                req.getTarget_id()
        );

        if (count != null && count > 0) {
            // 이미 공감/신고를 한 경우
            return -1L;
        }

        // 2) 중복이 아니라면 INSERT 실행
        String insertSql = """
        INSERT INTO REACTION 
        (RE_ID, MEMBER_ID, TARGET_TYPE, TARGET_ID, ACTION)
        VALUES (SEQ_REACTION.NEXTVAL, ?, ?, ?, ?)
    """;

        jdbc.update(insertSql,
                req.getMember_id(),
                req.getTarget_type(),
                req.getTarget_id(),
                req.getAction()
        );

        // 3) 방금 입력된 REACTION ID 반환
        return jdbc.queryForObject("SELECT SEQ_REACTION.CURRVAL FROM DUAL", Long.class);
    }

}
