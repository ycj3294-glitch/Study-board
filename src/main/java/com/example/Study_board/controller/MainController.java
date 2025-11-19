package com.example.Study_board.controller;


import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor

public class MainController {
    private final BoardService boardService;
    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("noticeList",
                boardService.getLatestPosts("board1", 7));

        model.addAttribute("freeList",
                boardService.getLatestPosts("board2", 7));

        model.addAttribute("codeList",
                boardService.getLatestPosts("board3", 7));

        model.addAttribute("studyList",
                boardService.getLatestPosts("board4", 7));

        return "communitytest";
    }
}
