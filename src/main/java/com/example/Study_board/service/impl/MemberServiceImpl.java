package com.example.Study_board.service.impl;


import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;
import com.example.Study_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberDao memberDao;

    @Override
    public long signup(MemberSignupReq req) {
        // 중복 이메일 예외 처리
        if(memberDao.findByEmail(req.getEmail()) != null) {
            throw new IllegalArgumentException("Email이 이미 있습니다.");
        }
        return memberDao.signup(req);
    }

    @Override
    public MemberRes login(String email, String pwd) {
        MemberRes member = memberDao.findByEmail(email);
        if (member == null || !member.getPwd().equals(pwd)) {
            throw new IllegalArgumentException("이메일이 존재하지 않거나 비밀번호가 맞지 않습니다.");
            return null;
        }
        return new MemberRes(member.getId(), member.getEmail(), member.getPwd(), member.getRedDate());
    }


    @Override
    public List<MemberRes> list() {
        List<MemberRes> list;
            try {
                list = memberDao.findAll();
            } catch (DataAccessException e) {
                log.error("회원 목록 조회 중 DB 예외 발생: {}", e.getCause());
                throw new IllegalArgumentException("회원 목록을 조회할 수 없습니다.");
            }
            return list;
        }

    @Override
    public MemberRes getByNickname(String nickname) {
        return null;
    }

    @Override
    public MemberRes getById(Long id) {
        return null;
    }

    @Override
    public MemberRes getByEmail(String email) {
        return null;
    }
}
