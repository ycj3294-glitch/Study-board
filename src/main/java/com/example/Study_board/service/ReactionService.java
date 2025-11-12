package com.example.Study_board.service;

import com.example.Study_board.dto.ReactionCreateReq;

public interface ReactionService {
    // 추천/신고 입력 기능
    Long recordReaction(ReactionCreateReq req);
}
