package com.example.Study_board.controller;

import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.service.BoardService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final String board1 = "board1";
    private final String board2 = "board2";
    private final String board3 = "board3";
    private final String board4 = "board4";
    // 자유게시판
    @GetMapping("/board1")  // 게시글 목록 가져 오기
    public String board1list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<BoardListRes> list = boardService.list(board1);
        model.addAttribute("posts", list);
        return "post/list";
    }
    // ?? 게시판
    @GetMapping("/board2")
    public String board2list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<BoardListRes> list = boardService.list(board2);
        model.addAttribute("posts", list);
        return "post/list";
    }
    // ?? 게시판
    @GetMapping("/board2")
    public String board3list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<BoardListRes> list = boardService.list(board3);
        model.addAttribute("posts", list);
        return "post/list";
    }
    // ?? 게시판
    @GetMapping("/board3")
    public String board4list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<BoardListRes> list = boardService.list(board4);
        model.addAttribute("posts", list);
        return "post/list";
    }


}
