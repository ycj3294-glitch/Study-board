package com.example.Study_board.controller;

import com.example.Study_board.dto.MemberRes;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        model.addAttribute("member", loginMember);
    }
}
