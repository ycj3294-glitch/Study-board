package com.example.Study_board.controller;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/board")
public class BoardRestController {
    private final BoardService boardService;

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Long> board(@RequestBody BoardCreateReq b) {
        return ResponseEntity.ok(boardService.board(b));
    }

    // 게시글 수정
    @PutMapping("/{board_id}")
    public ResponseEntity<Boolean> update(@PathVariable Long board_id, @RequestBody BoardCreateReq b) {
        return ResponseEntity.ok(boardService.update(board_id, b));
    }

    // 게시글 삭제
    @DeleteMapping("/{board_id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long board_id) {
//        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        return ResponseEntity.ok(boardService.delete(board_id));
    }

}
