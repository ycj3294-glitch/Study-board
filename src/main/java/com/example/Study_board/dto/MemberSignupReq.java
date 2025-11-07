package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

// 사전단계 : 사용자가 회원가입을 위해 데이터베이스에 전달하는 정보들
public class MemberSignupReq {
    private String email;
    private String pwd;
    private String nickname;
}
