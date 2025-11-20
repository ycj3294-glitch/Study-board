package com.example.Study_board.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BoardCreateReq {
    private String board_type;
    private Long member_id;
    private String title;
    private String contents;
    private String image_Url;
}