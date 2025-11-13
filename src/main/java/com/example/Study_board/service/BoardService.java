package com.example.Study_board.service;

import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;

import java.util.List;

public interface BoardService {
    // 게시글 작성
    Long board(BoardCreateReq b);
    // 게시글 목록
    List<BoardListRes> list(String board);

    // 게시글 수정
    boolean update(Long board_id, Long member_id, String title, String contents);
    // 게시글 삭제
    boolean delete(Long board_id,  Long member_id);

    // 게시글 전체 조회
    List<BoardListRes> findAll();
    // 공감글 조회
    List<BoardListRes> findTopLiked(int limit);


}
