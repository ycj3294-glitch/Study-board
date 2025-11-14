package com.example.Study_board.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor

public class MainController {
    @GetMapping("/")
    public String home() {
        return "communitytest";
    }
}
