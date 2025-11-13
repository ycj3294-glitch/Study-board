package com.example.Study_board.service.impl;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final MemberDao memberDao;
    private final BoardDao boardDao;

    // 게시글 작성
    @Override
    public Long board(BoardCreateReq b) {
        if (memberDao.findById(b.getMember_id()) == null) {
            throw new IllegalArgumentException("존재하지 않는 회원 입니다.");
        }
        return boardDao.save(b);
    }


    // 게시글 수정
    @Override
    public boolean update(Long board_id, BoardCreateReq b) {
        return boardDao.update(board_id, b.getTitle(), b.getContents());
    }

    // 게시글 삭제
    @Override
    public boolean delete(Long board_id) {
        try{
            Long writerId = boardDao.ByBoard_id(board_id);

            // 게시글이 존재하지 않는 경우
            if (writerId == null) {
                System.out.println("해당 게시글이 존재하지 않습니다. (board_id="+board_id+")");
                return false;
            }
            // 작성자 검증
//            if (!writerId.equals(board_id)) {
//                System.out.println("해당 게시글은 작성자만 게시글을 삭제할 수 있습니다.(요청자=" + member_id + ", 작성자=" + writerId + ")");
//                return false;
//            }

            // 4️⃣ 삭제 수행
            boolean deleted = boardDao.delete(board_id);
            if (deleted) {
                System.out.println("✅ 게시글 삭제 성공 (boardId=" + board_id + ")");
            } else {
                System.out.println("⚠️ 삭제 실패: DB 반영 안됨 (boardId=" + board_id + ")");
            }

            return deleted;
        } catch (Exception e) {
        System.out.println("🚨 게시글 삭제 중 오류 발생: " + e.getMessage());
        e.printStackTrace();
        return false;
        }
    }
}
