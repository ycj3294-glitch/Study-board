package com.example.Study_board.controller;




import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.SearchListRes;
import com.example.Study_board.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public String search(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,Model model) {
        List<SearchListRes> result = boardService.search(keyword);
        model.addAttribute("p",keyword);
        model.addAttribute("result",result);

        // 보여줄 페이지 데이터
        int start = Math.min(page * size, result.size());
        int end = Math.min(start + size, result.size());
        List<SearchListRes> subList = result.subList(start, end);

        Page<SearchListRes> searchPage = new PageImpl<>(subList, PageRequest.of(page, size), result.size());

        // 화면에 표시될 게시글 리스트(10개씩 나눈거)
        model.addAttribute("posts", subList);
        // 페이지네이션용 page 객체
        model.addAttribute("page", searchPage);
        return "/header/search";
    }
}
