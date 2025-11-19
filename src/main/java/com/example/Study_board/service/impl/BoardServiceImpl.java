package com.example.Study_board.service.impl;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.SearchListRes;
import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public List<BoardListRes> list(String board) {
        return boardDao.findAllByBoardType(board);
    }

    // 게시글 수정
    @Override
    public boolean update(Long board_id, Long member_id, String title, String contents) {
        try{
            Long writerId = boardDao.ByBoard_id(board_id);
            // 게시글 존재 여부 확인
            if (writerId == null) {
                log.warn("게시글이 존재하지 않습니다. (board_id={})", board_id);
                return false;
            }
            // 작성자 검증
            if (!Objects.equals(writerId, member_id)) {
                log.warn("수정 권한 없음 (요청자={}, 작성자={})", member_id, writerId);
                return false;
            }
            // 게시글 수정 실행
            boolean update = boardDao.update(board_id, title, contents);
            if (update) {
                log.info("게시글 수정 성공 (board_id={}, 수정자={})", board_id, member_id);
            } else {
                log.warn("게시글 수정 실패: DB 반영 안됨 (board_id={})", board_id);
            }
            return update;
        }catch (Exception e){
            log.error("게시글 수정 중 오류 발생 (board_id={}, member_id={})", board_id, member_id, e);
            return false;
        }
    }

    // 게시글 삭제
    @Override
    public boolean delete(Long board_id, Long member_id) {
        try{
            Long writerId = boardDao.ByBoard_id(board_id);

            // 게시글이 존재하지 않는 경우
            if (writerId == null) {
                log.warn("해당 게시글이 존재하지 않습니다. (board_id="+board_id+")");
                return false;
            }
            // 작성자 검증
            if (!Objects.equals(writerId, member_id)) {
                log.warn("해당 게시글은 작성자만 게시글을 삭제할 수 있습니다.(요청자=" + member_id + ", 작성자=" + writerId + ")");
                return false;
            }

            // 4️⃣ 삭제 수행
            boolean deleted = boardDao.delete(board_id);
            if (deleted) {
                log.info("✅ 게시글 삭제 성공 (boardId=" + board_id + ")");
            } else {
                log.warn("⚠️ 삭제 실패: DB 반영 안됨 (boardId=" + board_id + ")");
            }

            return deleted;
        } catch (Exception e) {
        log.error("🚨 게시글 삭제 중 오류 발생: " + e);
        return false;
        }
    }


    // 게시판글 조회
    @Override
    public List<BoardListRes> getLatestPosts(String boardType, int limit) {
        return boardDao.findLatestByType(boardType, limit);
    }
    // 게시글 전체 조회
    @Override
    public List<BoardListRes> findAll() {
        return boardDao.findAll();
    }

    // 공감 순서로 조회
    @Override
    public List<BoardListRes> findTopLiked(int limit) {
        return boardDao.findTopLiked(limit);
    }

    // 검색 조회
    @Override
    public List<SearchListRes> search(String keyword) {
        return boardDao.search(keyword);
    }

}
