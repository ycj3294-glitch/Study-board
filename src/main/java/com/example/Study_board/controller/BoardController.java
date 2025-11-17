package com.example.Study_board.controller;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.dto.CommentRes;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.service.BoardService;
import com.example.Study_board.service.CommentService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardDao boardDao;
    private final BoardService boardService;
    private final CommentService commentService;
    private final String board1 = "board1";
    private final String board2 = "board2";
    private final String board3 = "board3";
    private final String board4 = "board4";


    // 각 게시판 이동
    @GetMapping("/{boardType}")  // 게시글 목록 가져 오기
    public String boardlist(@PathVariable String boardType, HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";  // 세션이 없으면 로그인 페이지로 이동
        List<BoardListRes> list = boardService.list(boardType);
        model.addAttribute("posts", list);
        return "board/list";
    }

    // 게시글 상세 보기
    @GetMapping("/detail/{id}") // 현재 이동시 주소 http://localhost:8112/posts/detail/id
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        // 로그인 여부 확인
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";
        // 해당 게시글 정보 가져옴
        BoardRes post = boardDao.findByBoardID(id);
        if (post == null) {
            model.addAttribute("error", "해당 게시글이 존재하지 않습니다.");
            return "post/error"; // 없는 경우 따로 처리 가능
        }
        // 게시글 정보를 모델링
        model.addAttribute("post", post);

        // 해당 게시글 코멘트 리스트 정보를 가져옴
        List<CommentRes> comment = commentService.listByBoardid(id);
        if (comment == null) {
            comment = new ArrayList<>();
            model.addAttribute("comment", comment);
        }

        // 코멘트 정보를 모델링
        model.addAttribute("comment", comment);
        return "board/detail"; // detail.html 템플릿으로 이동
    }



}
