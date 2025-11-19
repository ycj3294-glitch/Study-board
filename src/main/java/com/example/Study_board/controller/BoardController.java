package com.example.Study_board.controller;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dto.*;
import com.example.Study_board.service.BoardService;
import com.example.Study_board.service.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardDao boardDao;
    private final BoardService boardService;
    private final CommentService commentService;
    private final String board1 = "board1";
    private final String board2 = "board2";
    private final String board3 = "board3";
    private final String board4 = "board4";


    // 각 게시판 이동
    @GetMapping("/{boardType}")  // 게시글 목록 가져 오기
    public String boardlist(@PathVariable String boardType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";  // 세션이 없으면 로그인 페이지로 이동
        // 게시판에 표시될 게시글 리스트
        List<BoardListRes> list = boardService.list(boardType);
        // 보여줄 페이지 데이터
        int start = Math.min(page * size, list.size());
        int end = Math.min(start + size, list.size());
        List<BoardListRes> subList = list.subList(start, end);

        Page<BoardListRes> boardPage = new PageImpl<>(subList, PageRequest.of(page, size), list.size());

        // 화면에 표시될 게시글 리스트(10개씩 나눈거)
        model.addAttribute("posts", subList);
        // 페이지네이션용 page 객체
        model.addAttribute("page", boardPage);
        // 해당게시판 타입 모델로 저장
        model.addAttribute("boardType", boardType);

        return "board/list";
    }

    // 게시글 상세 보기
    @GetMapping("/detail/{id}") // 현재 이동시 주소 http://localhost:8112/posts/detail/id
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        // 로그인 여부 확인
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";

        // 조회수 증가 먼저 실행
        boardService.increaseViewCount(id);

        // 해당 게시글 정보 가져옴
        BoardRes post = boardDao.findByBoardID(id);
        if (post == null) {
            model.addAttribute("error", "해당 게시글이 존재하지 않습니다.");
            return "post/error"; // 없는 경우 따로 처리 가능
        }
        // 게시글 정보를 모델링
        model.addAttribute("post", post);

        // 해당 게시글 코멘트 리스트 정보를 가져옴
        List<CommentRes> comment = commentService.listByBoardid(id);
        if (comment == null) {
            comment = new ArrayList<>();
            model.addAttribute("comment", comment);
        }

        // 코멘트 정보를 모델링
        model.addAttribute("comment", comment);
        return "board/detail"; // detail.html 템플릿으로 이동
    }

    // 게시글 작성 페이지로 이동
    @GetMapping("/{board_type}/new")
    public String moveboard(@PathVariable String board_type, Model model) {

        model.addAttribute("boardType", board_type);

        String boardName = switch (board_type) {
            case "board1" -> "공지사항";
            case "board2"   -> "자유게시판";
            case "board3"   -> "코드게시판";
            case "board4"  -> "스터디게시판";
            default       -> "게시판";
        };
        model.addAttribute("boardName", boardName);
        return "board/post";
    }


    // 게시글 작성
    @PostMapping("/{board_type}/new")
    public String crateboard(@PathVariable String board_type, BoardCreateReq req, HttpSession session, Model model) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if(member == null) {
            return "redirect:/login/login";
        }
        try {
            req.setBoard_type(board_type);
            req.setMember_id (member.getId()); // 화면에서 정보를 입력받을 수 없기 때문에 세션정보에서 추출해서 넣어 줌
            Long board_id = boardService.board(req);
            return "redirect:/board/" + req.getBoard_type();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "board/post";
        }
    }
    // 게시글 수정 페이지 이동
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long boardId,
                           HttpSession session,
                           Model model) {

        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        // 기존에 있는 메서드 사용 (findById 창조하지 않음)
        BoardRes board = boardDao.findByBoardID(boardId);
        if (board == null) {
            return "error/404";
        }

        // 작성자 검증
        if (!(loginMember.getId()==board.getMember_id())) {
            return "error/403";
        }
        model.addAttribute("post", board);
        return "board/edit";
    }
    // 게시글 수정
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable("id") Long boardId,
                             @RequestParam String title,
                             @RequestParam String contents,
                             HttpSession session) {

        Long loginMemberId = (Long) session.getAttribute("loginMember");

        boolean updated = boardService.update(boardId, loginMemberId, title, contents);

        if (!updated) {
            return "error/403";  // 권한 없거나 실패
        }

        return "redirect:/board/detail/" + boardId;
    }
    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long boardId,
                             HttpSession session) {

        Long loginMemberId = (Long) session.getAttribute("loginMember");

        boolean deleted = boardService.delete(boardId, loginMemberId);

        if (!deleted) {
            return "error/403";
        }

        return "redirect:/board/";
    }
}
