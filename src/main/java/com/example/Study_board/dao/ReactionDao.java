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
    // 리액션 기록
    public Long recordReaction(ReactionCreateReq req) {

        // 1) 중복 체크 (액션 포함)
        String checkSql = """
            SELECT COUNT(*) 
            FROM REACTION
            WHERE MEMBER_ID = ?
              AND TARGET_TYPE = ?
              AND TARGET_ID = ?
              AND ACTION = ?
        """;

        Integer count = jdbc.queryForObject(checkSql, Integer.class,
                req.getMember_id(),
                req.getTarget_type(),
                req.getTarget_id(),
                req.getAction()
        );

        if (count != null && count > 0) {
            log.info("이미 해당 액션 존재: memberId={}, targetType={}, targetId={}, action={}",
                    req.getMember_id(), req.getTarget_type(), req.getTarget_id(), req.getAction());
            return -1L; // 이미 해당 액션 존재
        }

        // 2) INSERT 실행
        String insertSql = """
            INSERT INTO REACTION (REACTION_ID, MEMBER_ID, TARGET_TYPE, TARGET_ID, ACTION)
            VALUES (SEQ_STUDY_REACTION.NEXTVAL, ?, ?, ?, ?)
        """;

        int rows = jdbc.update(insertSql,
                req.getMember_id(),
                req.getTarget_type(),
                req.getTarget_id(),
                req.getAction()
        );

        log.info("REACTION INSERT 완료: rowsAffected={}, memberId={}, targetType={}, targetId={}, action={}",
                rows, req.getMember_id(), req.getTarget_type(), req.getTarget_id(), req.getAction());

        // 3) 방금 입력된 REACTION_ID 반환
        Long reactionId = jdbc.queryForObject("SELECT SEQ_STUDY_REACTION.CURRVAL FROM DUAL", Long.class);
        log.debug("생성된 REACTION_ID: {}", reactionId);

        return reactionId;
    }
}