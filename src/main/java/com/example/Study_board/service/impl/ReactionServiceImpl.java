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
        if(!("board".equals(req.getTarget_type()) || "comment".equals(req.getTarget_type()))) {
            throw new RuntimeException("해당 type이 없습니다.");
        }
        return reactionDao.recordReaction(req);
    }

}
