package com.example.Study_board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchListRes {
    private Long boardId;
    private String title;
    private String snippet;
    private String nickname;
    private LocalDateTime regDate;
}
