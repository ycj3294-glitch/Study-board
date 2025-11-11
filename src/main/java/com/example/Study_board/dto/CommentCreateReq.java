package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CommentCreateReq {
    private Long board_id;
    private Long member_id;
    private String contents;
}
