package com.example.Study_board.controller;




import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.SearchListRes;
import com.example.Study_board.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.image.Kernel;
import java.util.List;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final BoardService boardService;

    // 검색 기능
    @GetMapping
    public String search(@RequestParam String keyword, Model model) {
        List<SearchListRes> result = boardService.search(keyword);
        model.addAttribute("p",keyword);
        model.addAttribute("result",result);

        return "/header/search";
    }
}
