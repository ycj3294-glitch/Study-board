package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

// 사전단계 : 로그인 할 때 사용자가 데이터베이스에 전달할 정보들
public class LoginReq {
    private String email;
    private String pwd;
}
