package com.example.Study_board.controller;

import com.example.Study_board.dto.ReactionCreateReq;
import com.example.Study_board.service.ReactionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
@Slf4j
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<Long> handleReaction(@RequestBody ReactionCreateReq req, HttpSession session) {

        // 로그인 체크
        Object login = session.getAttribute("loginmember");
        if (login == null) {
            return ResponseEntity.status(401).body(-1L);
        }

        log.info("REACTION 용청 : {}", req);

        Long result = reactionService.recordReaction(req);

        return ResponseEntity.ok(result);
    }
}
