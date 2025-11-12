package com.example.Study_board.dao;

import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.dto.CommentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
@Slf4j

public class CommentDao {
    private final JdbcTemplate jdbc;
    // 코멘트 작성
    public Long save(CommentCreateReq c){
        @Language("SQL")
                String sql = """
INSERT INTO STUDY_COMMENT (COMMENT_ID, BOARD_ID, MEMBER_ID, CONTENTS) 
VALUES(SEQ_STUDY_COMMENT.nextval, ?, ?, ? )
    """;
        jdbc.update(sql,c.getBoard_id(),c.getMember_id(),c.getContents());
        return jdbc.queryForObject("SELECT seq_study_comment.CURRVAL FROM dual", Long.class);
    }

    // 개별 게시글(BOARD_id)로 코멘트 조회

    // 코멘트 수정
    // 코멘트 삭제
    // mapper
    static class CommentRowMapper implements RowMapper<CommentRes> {

        @Override
        public CommentRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CommentRes(
                    rs.getLong("BOARD_ID"),
                    rs.getLong("MEMBER_ID"),
                    rs.getString("NICKNAME"),
                    rs.getString("CONTENTS"),
                    rs.getLong("LIKE_COUNT"),
                    rs.getLong("REPORT_COUNT"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }
}
