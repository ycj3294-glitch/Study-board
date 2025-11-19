package com.example.Study_board.controller;

import com.example.Study_board.dto.ReactionCreateReq;
import com.example.Study_board.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class ReactionController {
    private final ReactionService reactionService;

}
