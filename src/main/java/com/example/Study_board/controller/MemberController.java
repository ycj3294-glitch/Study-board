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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Member;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j

public class MemberController {
    private final MemberService memberService;

    // 로그인 페이지 이동
    @GetMapping // 루트 경로 localhost:8111/login
    public String loginPage() {
        return "login/login"; // resource/template/login.login.html 만들어줘야함
    }

    // 로그인 처리
    @PostMapping()
    public String login(@ModelAttribute LoginReq req, HttpSession session, Model model) {
        log.info("로그인 정보 : {}", req);
        MemberRes member = memberService.login(req.getEmail(), req.getPwd());
        if (member == null) {
            model.addAttribute("error", "이메일 혹은 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }
        session.setAttribute("loginMember", member);
        return "redirect:/";
    }

    // 로그아웃 버튼 기능
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 저장된 세션 정보를 무효화 시킴
        return "redirect:/";
    }

    // 회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupPage() {
        return "login/signup";
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

    // 회원 정보 페이지 표시
    @GetMapping("/member/info")
    public String memberInfo(HttpSession session, Model model) {
        // 로그인된 회원 정보 가져오기
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        // 로그인이 안되어 있으면 로그인 페이지로 이동
        if (member == null) {
            return "redirect:/login";
        }

        // 회원 정보를 model에 담아 HTML에서 출력하도록 전달
        model.addAttribute("member",member);

        // memberinfo.html로 페이지 이동
        return "memberinfo";
    }
    // 회원 정보 수정
    @PostMapping("/member/update")
    public String updateMember(@RequestParam String nickname, @RequestParam String pwd, @RequestParam(required = false) MultipartFile profileImage, HttpSession session, Model model) {
        // 세션에서 현재 로그인 회원 가져오기
        MemberRes loginmember = (MemberRes) session.getAttribute("loginMember");

        if (loginmember == null) {
            return "redirect:/login";
        }

        // 프로필 이미지 저장 처리
        String savedPath = loginmember.getProfilePath();  // 기본 이미지 유지 기본값
        if (profileImage != null && !profileImage.isEmpty()) {
            String uploadDir = "src/main/resources/static/upload/profile/";
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String fileName = loginmember.getId() + "_" + profileImage.getOriginalFilename();
            File saveFile = new File(uploadDir + fileName);

            try {
                profileImage.transferTo(saveFile);
                savedPath = "/upload/profile/" + fileName; // 웹에서 접근하는 경로
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
            }
        }
        // 비밀번호 변경 안 했으면 기존 값 사용
        if (pwd == null || pwd.trim().isEmpty()) {
            pwd = loginmember.getPwd();
        }

        // 서비스 계층에 수정 요청(id + 새 닉네임 + 새 비밀번호)
        boolean success = memberService.updateProfile(
                loginmember.getId(),
                nickname,
                pwd,
                savedPath
        );

        if (!success) {
            model.addAttribute("error", "회원 수정 중 문제가 발생했습니다.");
            return "memberinfo";
        }
        // DB에서 최신 정보 다시 조회 -> 세션 갱신
        MemberRes updated = memberService.getById(loginmember.getId());
        session.setAttribute("loginMember", updated);
        // 다시 내 정보 페이지로
        return "redirect:/member/info";
    }

    @GetMapping("/member/dele")
    public String deleteMember(HttpSession session) {
        MemberRes loginmember = (MemberRes) session.getAttribute("loginMember");
        if (loginmember == null) {
            return "redirect:/login";
        }
        boolean deleted = memberService.delete(loginmember.getId());

        if (deleted) {
            session.invalidate();
            return "redirect:/";
        }
        return "redirect:/member/info?error=delete-failed";
    }
}
