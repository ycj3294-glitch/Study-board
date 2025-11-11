package com.example.Study_board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BoardListRes {
    private Long board_id;
    private Long member_id;
    private String nickname;
    private String title;
    private Long view_count;
    private Long like_count;
    private Long report_count;
    private LocalDateTime reg_date;
}
