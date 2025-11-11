package com.example.Study_board.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BoardCreateReq {
    private String board_type;
    private String member_id;
    private String title;
    private String contents;
}