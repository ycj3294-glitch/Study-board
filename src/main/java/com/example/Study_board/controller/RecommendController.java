package com.example.Study_board.controller;

import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendController {

    private final BoardService boardService;

    // ✅ 공감(좋아요) 순 상위 10개
    @GetMapping("/like")
    public String bestByLike(Model model) {
        List<BoardListRes> posts = boardService.findTopLiked(10);
        model.addAttribute("posts", posts);
        model.addAttribute("title", "공감 많은 게시글");
        return "recommend/like"; // recommend/like.html
    }
}
