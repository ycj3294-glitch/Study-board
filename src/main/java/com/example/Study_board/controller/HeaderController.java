package com.example.Study_board.controller;

import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/header")
public class HeaderController {

    private final BoardService boardService;

    @GetMapping("/listall")
    public String listAll(Model model) {
        // 모든 게시글 리스트 불러옴

        // 모델에 게시글 리스트 입력

        // 모델 명 지정

        return "header/listall"; // header/listall.html
    }

    // ✅ 공감(좋아요) 순 상위 10개
    @GetMapping("/like")
    public String bestByLike(Model model) {
        // 공감 순서로 게시글을 불러옴
//        List<BoardListRes> posts = boardService.findTopLiked(10);
        // 모델에 게시글 리스트를 입력
//        model.addAttribute("posts", posts);
        // 모델 명 지정
//        model.addAttribute("title", "공감 많은 게시글");
        return "header/like"; // header/like.html
    }

    // 홈으로 이동(html a 링크 조작으로 가능)


    // 검색 기능
    @GetMapping("/search/{question}")
    public String search(@PathVariable String question, Model model) {
        // 검색문구에 따른 결과를 불러옴

        // 모델에 결과 리스트를 입력

        // 모델 명 지정

        return "header/search"; // templates/header/search.html 현재 안 만들어놓음
    }
}
