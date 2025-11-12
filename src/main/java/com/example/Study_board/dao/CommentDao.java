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
import java.util.List;

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
    // 코멘트 삭제
    public boolean delete(Long id){
        @Language("SQL")
                String sql = "DELETE FROM comments WHRER id =?";
        return jdbc.update(sql,id) > 0;
    }
    //board id 검색(리스트 반환, 게시글 조회시 표시되는 해당 글의 코멘트 리스트)
    public List<CommentRes> findById(Long id){
        @Language("SQL")
        String sql = """        
             SELECT
             c.BOARD_ID,
             c.MEMBER_ID,
             m.NICKNAME,
             c.CONTENTS,
             NVL(l.LIKE_COUNT, 0) AS LIKE_COUNT,
             NVL(r.REPORT_COUNT, 0) AS REPORT_COUNT,
             c.REG_DATE
            FROM STUDY_COMMENT c
            JOIN STUDY_MEMBER m ON c.MEMBER_ID = m.MEMBER_ID
            LEFT JOIN (
                SELECT
                    TARGET_ID,
                    COUNT(*) AS LIKE_COUNT
                FROM REACTION
                WHERE UPPER(TARGET_TYPE) = 'COMMENT'
                  AND UPPER(ACTION) = 'LIKE'
                GROUP BY TARGET_ID
            ) l ON c.COMMENT_ID = l.TARGET_ID
            LEFT JOIN (
                SELECT
                    TARGET_ID,
                    COUNT(*) AS REPORT_COUNT
                FROM REACTION
                WHERE UPPER(TARGET_TYPE) = 'COMMENT'
                  AND UPPER(ACTION) = 'REPORT'
                GROUP BY TARGET_ID
            ) r ON c.COMMENT_ID = r.TARGET_ID
            WHERE c.BOARD_ID = ?
            ORDER BY c.REG_DATE ASC
        """;
        List<CommentRes> list = jdbc.query(sql, new CommentRowMapper(), id);
        return list;
    }


    // 개별 게시글(BOARD_id)로 코멘트 조회
    // 코멘트 수정
    public boolean update(CommentCreateReq c, Long id){
        @Language("SQL")
                String sql = "UPDATE STUDY_COMMENT SET CONTENTS = ? WHERE COMMENT_ID =?";
        return jdbc.update(sql, c.getContents(), id) > 0;
    }

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
