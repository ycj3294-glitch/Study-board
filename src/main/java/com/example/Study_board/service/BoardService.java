package com.example.Study_board.service;

import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.dto.SearchListRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    // 게시글 작성
    Long board(BoardCreateReq b, MultipartFile imageFile);
    // 게시글 목록
    List<BoardListRes> list(String board);

    // 게시글 수정
    boolean update(Long board_id, Long member_id, String title, String contents, String image_Url);
    // 게시글 삭제
    boolean delete(Long board_id,  Long member_id);

    // 게시글 조회수 증가
    void increaseViewCount(Long id);

    // 게시판글 조회
    List<BoardListRes> getLatestPosts(String boardType, int limit);

    // 게시글 전체 조회
    List<BoardListRes> findAll();
    // 공감글 조회
    List<BoardListRes> findTopLiked(int limit);

    // 검색 조회
    List<SearchListRes> search(String keyword);

    // board_id로 게시글 정보 조회
    public BoardRes getboardRes(Long board_id);

    // 게시글 수정
    boolean update(Long board_id,
                   Long member_id,
                   String title,
                   String contents,
                   MultipartFile newImageFile, // 새로 업로드된 파일
                   boolean deleteImage);
}
