package com.example.Study_board.controller;

import com.example.Study_board.dto.MemberRes;
import com.example.Study_board.dto.MemberSignupReq;
import com.example.Study_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members") // localport:XXXX/api/members 주소에서 실행될 수 있는 기능,
                                    // 각 기능별 mapping 뒤의 주소를 추가하면 해당 기능이 지정됨
@Slf4j

// 4단계 : 기능테스트, 제대로 기능이 잘 돌아가는지 확인
public class MemberRestController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberSignupReq req) {
        log.error("회원 가입 : {}", req);
        return ResponseEntity.ok(memberService.signup(req));
    }

    // 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberRes>> list() {
        return ResponseEntity.ok(memberService.list());
    }

    // 이메일로 회원 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<MemberRes> emaillist(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getByEmail(email));
    }

    // 닉네임으로 회원 조회
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<List<MemberRes>> nicknamelist(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.getByNickname(nickname));
    }
}
