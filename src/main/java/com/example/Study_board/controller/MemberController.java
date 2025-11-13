package com.example.Study_board.controller;

import com.example.Study_board.dto.LoginReq;
import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;
import com.example.Study_board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j

public class MemberController {
    private final MemberService memberService;

    // 로그인 페이지 이동
    @GetMapping("/") // 루트 경로 localhost:8111
    public String loginPage() {
        return "login/login"; // resource/template/login.login.html 만들어줘야함
    }
    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginReq req, HttpSession session, Model model) {
        log.info("로그인 정보 : {}", req);
        MemberRes member = memberService.login(req.getEmail(), req.getPwd());
        if (member == null) {
            model.addAttribute("error", "이메일 혹은 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }
        session.setAttribute("loginMember", member);
        return "redirect:/home";
    }

    // 회원가입 페이지 이동
    @GetMapping("/new")
    public String signupPage() {
        return "login/new";
    }

    // 회원 가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute MemberSignupReq req, Model model) {
        Long member = memberService.signup(req);
        if(member == null) {
            model.addAttribute("error", "이미 있는 이메일 입니다.");
            return "login/new";
        }

        return "login/login";
    }

    // 전체 회원 목록 처리
    @GetMapping("/list")
    public String memberlist(HttpSession session, Model model) {
        var loginMember = session.getAttribute("loginMember"); // 세션이 존재하는지 확인
        List<MemberRes> list = memberService.list();
        model.addAttribute("member",list);
        return "login/list";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
