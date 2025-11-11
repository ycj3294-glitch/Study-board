package com.example.Study_board.controller;

import com.example.Study_board.dto.CommentCreateReq;
import com.example.Study_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
