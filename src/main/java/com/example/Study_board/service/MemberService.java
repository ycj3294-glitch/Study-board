package com.example.Study_board.service;

import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;

import java.util.List;

// 2단계 : 어떤 기능을 구현할지 설계 단계

public interface MemberService {
    // 회원가입
    long signup(MemberSignupReq req);

    // 로그인
    MemberRes login(String email, String pwd);

    // 회원 목록 보기
    List<MemberRes> list();

    // 닉네임으로 회원 조회
    List<MemberRes> getByNickname(String nickname);

    // member_id로 회원 조회
    MemberRes getById(Long id);

    // email로 회원 조회
    MemberRes getByEmail(String email);
}
