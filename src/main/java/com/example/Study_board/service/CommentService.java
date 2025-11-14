package com.example.Study_board.service;

import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.dto.CommentRes;

import java.util.List;

public interface CommentService {
    // 코멘트 쓰기
    Long write(CommentCreateReq req);
    // 코멘트 삭제
    boolean delete(Long id);
    // 코멘트 수정
    boolean update(CommentCreateReq req,Long id);
    // 해당 게시글 코멘트 조회
    List<CommentRes> listByBoardid(Long board_id);
}
