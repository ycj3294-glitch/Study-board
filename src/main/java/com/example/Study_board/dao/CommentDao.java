package com.example.Study_board.dao;

import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.dto.CommentRes;
import com.example.Study_board.dto.MemberRes;
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

public class CommentDao {
    private final JdbcTemplate jdbc;

    // 코멘트 작성 (테이블 생성에 insert 구문 예시 있음)
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
