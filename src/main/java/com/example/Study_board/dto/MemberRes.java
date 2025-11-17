package com.example.Study_board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

// 사전단계 : 데이터베이스가 사용자에게 전달할 정보들
public class MemberRes {
    private long id;
    private String email;
    private String nickname;
    private LocalDateTime redDate;
    private String profilePath;
    private String pwd;
}
