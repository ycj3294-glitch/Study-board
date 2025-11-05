package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginReq {
    private String email;
    private String pwd;
}
