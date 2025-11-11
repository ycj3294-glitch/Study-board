package com.example.Study_board.dao;

import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
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

public class BoardDao {
    private final JdbcTemplate jdbc;
    // 게시판별 조회
    public List<BoardListRes> findAllByBoardType(String board_type) {
        @Language("SQL")
        String sql = """
                SELECT\s
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
                GROUP BY\s
                    b.BOARD_ID, b.MEMBER_ID, m.NICKNAME, b.TITLE, b.VIEW_COUNT, b.REG_DATE
                ORDER BY b.REG_DATE DESC;
                """;
        List<BoardListRes> list = jdbc.query(sql, new BoardListRowMapper(), board_type);
        return list;
    }

    // 게시글 조회
    public BoardRes findByBoardID(Long board_id) {
        @Language("SQL")
        String sql = """
                SELECT t.*, b.CONTENTS
        FROM (
            SELECT\s
                b.BOARD_ID,
                b.MEMBER_ID,
                m.NICKNAME,
                b.TITLE,
                b.VIEW_COUNT,
                NVL(SUM(CASE WHEN r.ACTION = 'LIKE' THEN 1 ELSE 0 END),0) AS LIKE_COUNT,
                NVL(SUM(CASE WHEN r.ACTION = 'REPORT' THEN 1 ELSE 0 END),0) AS REPORT_COUNT,
                b.REG_DATE
            FROM STUDY_BOARD b
            JOIN STUDY_MEMBER m ON b.MEMBER_ID = m.MEMBER_ID
            LEFT JOIN REACTION r\s
                ON r.TARGET_TYPE='BOARD' AND r.TARGET_ID=b.BOARD_ID
            WHERE b.BOARD_ID = ?
            GROUP BY b.BOARD_ID, b.MEMBER_ID, m.NICKNAME, b.TITLE, b.VIEW_COUNT, b.REG_DATE
        ) t
        JOIN STUDY_BOARD b ON b.BOARD_ID = t.BOARD_ID;
""";
        List<BoardRes> list = jdbc.query(sql, new BoardResRowMapper(), board_id);
        return list.isEmpty() ? null : list.get(0);
    }
    // 게시글 작성
    // 게시글 수정
    // 게시글 삭제


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
            return new BoardRes(
                    rs.getLong("BOARD_ID"),
                    rs.getLong("MEMBER_ID"),
                    rs.getString("TITLE"),
                    rs.getString("NICKNAME"),
                    rs.getString("CONTENTS"),
                    rs.getLong("VIEW_COUNT"),
                    rs.getLong("LIKE_COUNT"),
                    rs.getLong("REPORT_COUNT"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }

}
