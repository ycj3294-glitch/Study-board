package com.example.Study_board.controller;

import ch.qos.logback.core.model.Model;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.ReactionCreateReq;
import com.example.Study_board.service.CommentService;
import com.example.Study_board.service.ReactionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reaction")
@Slf4j
public class ReactionController {
    private final ReactionService reactionService;
    private final CommentService commentService;


    @PostMapping("/like")
    public String likeReaction(ReactionCreateReq req, HttpSession session, RedirectAttributes redirectAttrs) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";

        req.setMember_id(loginMember.getId()); // 로그인 정보 세팅
        Long result = reactionService.recordReaction(req);

        if (result == -1L) {
            redirectAttrs.addFlashAttribute("message", "이미 공감하셨습니다.");
        }

        // 댓글이면 게시글 ID로 변환
        Long redirectId = req.getTarget_type().equals("BOARD")
                ? req.getTarget_id()
                : commentService.findByBoardid(req.getTarget_id());

        return "redirect:/board/detail/" + redirectId;
    }

    @PostMapping("/report")
    public String reportReaction(ReactionCreateReq req, HttpSession session, RedirectAttributes redirectAttrs) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";

        req.setMember_id(loginMember.getId()); // 로그인 정보 세팅
        Long result = reactionService.recordReaction(req);

        if (result == -1L) {
            redirectAttrs.addFlashAttribute("message", "이미 신고하셨습니다.");
        }

        // 댓글이면 게시글 ID로 변환
        Long redirectId = req.getTarget_type().equals("BOARD")
                ? req.getTarget_id()
                : commentService.findByBoardid(req.getTarget_id());

        return "redirect:/board/detail/" + redirectId;
    }
}


