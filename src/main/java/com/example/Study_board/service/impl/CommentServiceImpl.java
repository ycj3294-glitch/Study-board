package com.example.Study_board.service.impl;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dao.CommentDao;
import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final BoardDao boardDao;
    private final MemberDao memberDao;

    @Override
    public Long write(CommentCreateReq req) {
        if (boardDao.findByBoardID(req.getBoard_id()) == null) {
            throw new IllegalArgumentException("게시글이 없습니다. board_id=" + req.getBoard_id());
        }
        if (memberDao.findById(req.getMember_id()) == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다");
        }
        return commentDao.save(req);
    }
    // 확인
}