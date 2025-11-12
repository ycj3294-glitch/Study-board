package com.example.Study_board.service.impl;

import com.example.Study_board.dao.ReactionDao;
import com.example.Study_board.dto.ReactionCreateReq;
import com.example.Study_board.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ReactionServiceImpl implements ReactionService {
    private final ReactionDao reactionDao;
    @Override
    @Transactional
    public Long recordReaction(ReactionCreateReq req) {
        if(req.getTarget_type() == "board") {
//            req.setTarget_id() = board_id;
        } else if(req.getTarget_type() == "comment") {
//            req.setTarget_id() = comment_id;
        } else {
            throw new RuntimeException("해당 type이 없습니다.");
        }
        return reactionDao.recordReaction(req);
    }
}
