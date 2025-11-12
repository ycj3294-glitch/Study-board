package com.example.Study_board.dao;

import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.intellij.lang.annotations.Language;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j

// 1단계
// 데이터베이스랑 연결되어서 직접 쿼리문을 날려주는 기능을 담당, 각 기능별로 데이터베이스에 정보를 저장하거나 저장된 정보를 조회

public class MemberDao {
    private final JdbcTemplate jdbc;

    // 회원 가입 sql
    @Transactional
    public Long signup(MemberSignupReq m) {
        @Language("SQL")
        String sql = """
        INSERT INTO STUDY_MEMBER(MEMBER_ID, EMAIL, PWD, NICKNAME) VALUES (SEQ_STUDY_MEMBER.NEXTVAL, ?, ?, ?)
        """;
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getNickname());
        return jdbc.queryForObject("SELECT SEQ_STUDY_MEMBER.CURRVAL FROM DUAL", Long.class);
        }
    // 로그인
    public boolean login(String email, String pwd) {
        @Language("SQL")
        String sql = """
            SELECT * FROM STUDY_MEMBER WHERE EMAIL = ? AND PWD = ?
            """;
        MemberRes memberRes = jdbc.queryForObject(sql, new MemberRowMapper(), email, pwd);
        return (memberRes != null);
    }
    // 이메일로 회원 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = """
        SELECT * FROM STUDY_MEMBER WHERE EMAIL=?
        """;
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0); // 조회시 결과가 없는 경우 null을 넣기 위해서
    }
        // MEMBER_ID로 회원 조회(사용할지 안할지 의문)
    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = """
        SELECT * FROM STUDY_MEMBER WHERE MEMBER_ID=?
        """;
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    // nickname으로 회원 조회
    public List<MemberRes> findByNickname(String nickname) {
        @Language("SQL")
        String sql = """        
        SELECT * FROM STUDY_MEMBER WHERE NICKNAME =?
        """;
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), nickname);
        return list.isEmpty() ? null : list;
    }
    // 전체 회원 조회
    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = """
        SELECT * FROM STUDY_MEMBER ORDER BY MEMBER_ID DESC
        """;
        return jdbc.query(sql, new MemberRowMapper());
    }
    // Mapper
    static class MemberRowMapper implements RowMapper<MemberRes> {

        @Override
        public MemberRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberRes(
                    rs.getLong("MEMBER_ID"),
                    rs.getString("EMAIL"),
                    rs.getString("NICKNAME"),
                    rs.getTimestamp("REG_DATE").toLocalDateTime()
            );
        }
    }
}
