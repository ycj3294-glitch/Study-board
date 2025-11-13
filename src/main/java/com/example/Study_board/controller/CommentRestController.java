package com.example.Study_board.controller;

import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
@Slf4j
public class CommentRestController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CommentCreateReq req){
        return ResponseEntity.ok(commentService.write(req));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        log.error("댓글 삭제 : {}",id);
        return ResponseEntity.ok(commentService.delete(id));
    }
    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id, @RequestBody CommentCreateReq req){
        return ResponseEntity.ok(commentService.update(req,id));
    }
}
