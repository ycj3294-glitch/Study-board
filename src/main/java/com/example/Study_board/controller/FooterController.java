package com.example.Study_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/footer")

public class FooterController {
    @GetMapping("/rule")
    public String rule() {
        return "footer/rule";
    }
    @GetMapping("/help")
    public String help() {
        return "footer/help";
    }
    @GetMapping("/creator")
    public String creator() {
        return "footer/creator";
    }
}
