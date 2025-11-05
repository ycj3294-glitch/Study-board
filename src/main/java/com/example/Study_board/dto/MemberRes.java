package com.example.Study_board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class MemberRes {
    private long id;
    private String email;
    private String nickname;
    private LocalDateTime redDate;
}
