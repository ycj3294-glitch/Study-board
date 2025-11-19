package com.example.Study_board.controller;

import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j

public class CommentController {
    private final CommentService commentService;


    // 댓글 작성
    @PostMapping("/{board_id}")
    public String create(@PathVariable long board_id, CommentCreateReq req, HttpSession session) {
        // 세션 정보 가져옴(멤버 아이디 필요해서)
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        // 경로의 board_id 설정
        req.setBoard_id(board_id);
        req.setMember_id(member.getId());
        commentService.write(req);
        return "redirect:/board/detail/" + board_id;

    }
    // 댓글 수정
    @PostMapping("edit/{comment_id}")
    public String update(@PathVariable long comment_id, CommentCreateReq req) {
        Long board_id = commentService.findByBoardid(comment_id);
        commentService.update(req, comment_id);
        return "redirect:/board/detail/" + board_id;
    }

    // 댓글 삭제
    @PostMapping("/delete/{comment_id}")
    public String delete(@PathVariable long comment_id) {
        Long board_id = commentService.findByBoardid(comment_id);
        commentService.delete(comment_id);
        return "redirect:/board/detail/" + board_id;
    }

}
