package com.example.Study_board.service.impl;


import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;
import com.example.Study_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

// 3단계 : MemberService의 설계를 구체적으로 구성, dao에서 만든 기능을 불러와서 시행하는 걸로 구현하며 시행하기 전 예외사항이 있으면 그를 반영

public class MemberServiceImpl implements MemberService {
    private final MemberDao memberDao; // 클래스를 불러와야 기능을 사용 가능

    @Override
    public long signup(MemberSignupReq req) {
        // 중복 이메일 예외 처리(예외사항)
        if (memberDao.findByEmail(req.getEmail()) != null) {
            throw new IllegalArgumentException("Email이 이미 있습니다.");
        }
        // 중복 닉네임 예외 처리
        if (memberDao.findByNickname(req.getNickname()) != null) {
            throw new IllegalArgumentException(("닉네임이 이미 있습니다."));
        }
        return memberDao.signup(req);
    }

    @Override
    public MemberRes login(String email, String pwd) {
        MemberRes member = memberDao.findByEmail(email);
        if (!member.getPwd().equals(pwd)) return null;
        return new MemberRes(member.getId(), member.getEmail(), member.getNickname(), member.getRedDate(), member.getProfilePath(), member.getPwd());
    }//패스워드 res에 일부러 안넣었는데 이거 비밀번호 조회가 꼭 필요한가?


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
    public List<MemberRes> getByNickname(String nickname) {
        List<MemberRes> list = memberDao.findByNickname(nickname);
        if (list == null) throw new IllegalArgumentException("해당 닉네임을 가진 회원은 없습니다.");
        return memberDao.findByNickname(nickname);
    }

    @Override
    public MemberRes getById(Long id) {
        MemberRes memberRes = memberDao.findById(id);
        if (memberRes == null) throw new IllegalArgumentException("입력 id에 해당하는 회원은 없습니다.");
        return memberDao.findById(id);
    }

    @Override
    public MemberRes getByEmail(String email) {
        MemberRes memberRes = memberDao.findByEmail(email);
        if (memberRes == null) throw new IllegalArgumentException("해당 이메일을 가진 회원은 없습니다.");
        return memberDao.findByEmail(email);
    }


    @Override
    public boolean updateProfile(long Id, String nickname, String pwd, String profilePath) {

        return memberDao.updateProfile(Id, nickname, pwd, profilePath) > 0;
    }

    @Override
    public boolean delete(long id) {
        try {
            return memberDao.delete(id);
        } catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
}