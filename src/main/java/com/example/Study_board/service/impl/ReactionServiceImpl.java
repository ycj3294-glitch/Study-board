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
        // 1) 대소문자 통일
        String targetType = req.getTarget_type().toUpperCase();
        String action = req.getAction().toUpperCase();

        // 2) 유효성 검사
        if(!("BOARD".equals(targetType) || "COMMENT".equals(targetType))) {
            throw new RuntimeException("유효하지 않은 TARGET_TYPE입니다.");
        }
        if(!("LIKE".equals(action) || "REPORT".equals(action))) {
            throw new RuntimeException("유효하지 않은 ACTION입니다.");
        }

        req.setTarget_type(targetType);
        req.setAction(action);

        // 3) DAO 호출
        return reactionDao.recordReaction(req);
    }

}
