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
    boolean update(Long board_id, BoardCreateReq b );
    // 게시글 삭제
    boolean delete(Long board_id);
    // 추천 수 증가
    // 공감 수 증가
    // 신고 수 증가
}
