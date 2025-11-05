package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberSignupReq {
    private String email;
    private String pwd;
    private String nickname;
}
