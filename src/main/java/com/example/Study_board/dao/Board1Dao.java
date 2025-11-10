package com.example.Study_board.dao;

import com.example.Study_board.dto.BoardRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
@Slf4j

public class Board1Dao {
    private final JdbcTemplate jdbc;
    // 게시글 작성
    // 게시글 수정
    // 게시글 삭제
    // 추천 수 증가
    // 공감 수 증가
    // 신고 수 증가
    // MAPPER
    static class BoardRowMapper implements RowMapper<BoardRes> {
        @Override
        public BoardRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BoardRes(
                    rs.getLong("BOARD_ID"),
                    rs.getString("TITLE"),
                    rs.getString("NICKNAME"),
                    rs.getString("CONTENTS"),
                    rs.getLong("LIKE_COUNT"),
                    rs.getLong("RECOMMEND_COUNT"),
                    rs.getLong("VIEW_COUNT"),
                    rs.getLong("REPORT_COUNT"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }

}
