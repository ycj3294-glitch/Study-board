package com.example.Study_board.service.impl;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dao.CommentDao;
import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.dto.CommentRes;
import com.example.Study_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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


    @Override
    public boolean delete(Long id) {
        return commentDao.delete(id);
    }
    @Override
    public boolean update(CommentCreateReq req, Long id){
        return commentDao.update(req, id);
    }

    @Override
    public Long findByBoardid(long comment_id) {
        return commentDao.findByCommentIdforBoardId(comment_id);
    }


    @Override
    public List<CommentRes> listByBoardid(Long board_id) {
        return commentDao.findById(board_id);
    }


}