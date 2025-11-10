package com.example.Study_board.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardReq {
    private String nickname;
    private String title;
    private String contents;
}