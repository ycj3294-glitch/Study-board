package com.example.Study_board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CommentRes {
    private Long comment_id;
    private Long board_id;
    private Long member_id;
    private String nickname;
    private String contents;
    private Long like_count;
    private Long report_count;
    private LocalDateTime reg_date;

}
