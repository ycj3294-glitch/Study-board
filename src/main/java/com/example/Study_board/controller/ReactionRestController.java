package com.example.Study_board.controller;

import com.example.Study_board.dto.ReactionCreateReq;
import com.example.Study_board.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reaction")
@Slf4j

public class ReactionRestController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<Long> recordReaction(@RequestBody ReactionCreateReq req) {
        log.info("입력값: {}", req);
        return  ResponseEntity.ok(reactionService.recordReaction(req));
    }

}
