package com.example.Study_board.service;

import com.example.Study_board.dto.CommentCreateReq;

public interface CommentService {
    // 게시글 쓰기
    Long write(CommentCreateReq req);
    // 게시글 삭제
    boolean delete(Long id);
    //게시글 수정
    boolean update(CommentCreateReq req,Long id);
}
