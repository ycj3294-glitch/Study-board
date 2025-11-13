package com.example.Study_board.dao;

import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j

public class BoardDao {
    private final JdbcTemplate jdbc;
    // 게시판별 조회
    public List<BoardListRes> findAllByBoardType(String board_type) {
        @Language("SQL")
        String sql = """
                SELECT
                    b.BOARD_ID,
                    b.MEMBER_ID,
                    m.NICKNAME,
                    b.TITLE,
                    b.VIEW_COUNT,
                    NVL(SUM(CASE WHEN r.ACTION = 'LIKE' THEN 1 ELSE 0 END), 0) AS LIKE_COUNT,
                    NVL(SUM(CASE WHEN r.ACTION = 'REPORT' THEN 1 ELSE 0 END), 0) AS REPORT_COUNT,
                    b.REG_DATE
                FROM STUDY_BOARD b
                JOIN STUDY_MEMBER m ON b.MEMBER_ID = m.MEMBER_ID
                LEFT JOIN REACTION r ON r.TARGET_TYPE = 'BOARD' AND r.TARGET_ID = b.BOARD_ID
                WHERE b.BOARD_TYPE = ?
                GROUP BY
                    b.BOARD_ID, b.MEMBER_ID, m.NICKNAME, b.TITLE, b.VIEW_COUNT, b.REG_DATE
                ORDER BY b.REG_DATE DESC
                """;
        List<BoardListRes> list = jdbc.query(sql, new BoardListRowMapper(), board_type);
        return list;
    }

    // 게시글 조회
    public BoardRes findByBoardID(Long board_id) {
        @Language("SQL")
        String sql = """
        SELECT
            t.BOARD_ID       AS BOARD_ID,
            t.MEMBER_ID      AS MEMBER_ID,
            t.NICKNAME       AS NICKNAME,
            t.TITLE          AS TITLE,
            t.VIEW_COUNT     AS VIEW_COUNT,
            t.LIKE_COUNT     AS LIKE_COUNT,
            t.REPORT_COUNT   AS REPORT_COUNT,
            t.REG_DATE       AS REG_DATE,
            b.CONTENTS       AS CONTENTS
        FROM (
            SELECT
                b.BOARD_ID,
                b.MEMBER_ID,
                m.NICKNAME,
                b.TITLE,
                b.VIEW_COUNT,
                NVL(SUM(CASE WHEN r.ACTION = 'LIKE' THEN 1 ELSE 0 END), 0) AS LIKE_COUNT,
                NVL(SUM(CASE WHEN r.ACTION = 'REPORT' THEN 1 ELSE 0 END), 0) AS REPORT_COUNT,
                b.REG_DATE
            FROM STUDY_BOARD b
            JOIN STUDY_MEMBER m ON b.MEMBER_ID = m.MEMBER_ID
            LEFT JOIN REACTION r
                ON r.TARGET_TYPE='BOARD' AND r.TARGET_ID=b.BOARD_ID
            WHERE b.BOARD_ID = ?
            GROUP BY b.BOARD_ID, b.MEMBER_ID, m.NICKNAME, b.TITLE, b.VIEW_COUNT, b.REG_DATE
        ) t
        JOIN STUDY_BOARD b ON b.BOARD_ID = t.BOARD_ID        
        """;
        List<BoardRes> list = jdbc.query(sql, new BoardResRowMapper(), board_id);
        return list.isEmpty() ? null : list.get(0);
    }
    // 게시글 작성
    public Long save(BoardCreateReq b) {
        @Language("SQL")
        String sql = """
        INSERT INTO STUDY_BOARD (BOARD_ID, BOARD_TYPE, MEMBER_ID, TITLE, CONTENTS) 
        VALUES(SEQ_STUDY_BOARD.nextval, ?, ?, ?, ?)
        """;
        jdbc.update(sql, b.getBoard_type(), b.getMember_id(), b.getTitle(), b.getContents());
        return jdbc.queryForObject("SELECT SEQ_STUDY_BOARD.CURRVAL FROM dual", Long.class);
    }
    // 게시글 수정
    public boolean update(Long board_id, String title, String contents) {
        @Language("SQL")
        String sql = "UPDATE STUDY_BOARD SET  TITLE = ?, CONTENTS = ? WHERE BOARD_ID = ?";
        return jdbc.update(sql, title, contents, board_id) > 0;
    }
    // 게시글 작성자 조회 (ByBoard_id)
    public Long ByBoard_id(Long board_id) {
        String sql = "SELECT MEMBER_ID FROM STUDY_BOARD WHERE BOARD_ID = ?";
        return jdbc.queryForObject(sql, Long.class, board_id);
    }
    // 게시글 삭제
    public boolean delete(Long board_id) {
        @Language("SQL")
        String sql = "DELETE FROM STUDY_BOARD WHERE BOARD_ID = ?";
        return jdbc.update(sql, board_id) > 0;
    }


    // list용 mapper
    static class BoardListRowMapper implements RowMapper<BoardListRes> {
        @Override
        public BoardListRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BoardListRes(
                    rs.getLong("BOARD_ID"),
                    rs.getLong("MEMBER_ID"),
                    rs.getString("TITLE"),
                    rs.getString("NICKNAME"),
                    rs.getLong("VIEW_COUNT"),
                    rs.getLong("LIKE_COUNT"),
                    rs.getLong("REPORT_COUNT"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }
    // 상세 보기용 MAPPER
    static class BoardResRowMapper implements RowMapper<BoardRes> {
        @Override
        public BoardRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            // 디버깅용
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                System.out.println(meta.getColumnLabel(i));
            }

            return new BoardRes(
                    rs.getLong("BOARD_ID"),
                    rs.getLong("MEMBER_ID"),
                    rs.getString("NICKNAME"),
                    rs.getString("TITLE"),
                    rs.getString("CONTENTS"),
                    rs.getLong("VIEW_COUNT"),
                    rs.getLong("LIKE_COUNT"),
                    rs.getLong("REPORT_COUNT"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }

}
