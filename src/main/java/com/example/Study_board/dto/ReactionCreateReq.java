package com.example.Study_board.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReactionCreateReq {
    private Long member_id;
    private String target_type;
    private Long target_id;
    private String action;
}

