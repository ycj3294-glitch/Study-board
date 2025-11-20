package com.example.Study_board.service.impl;

import com.example.Study_board.dao.BoardDao;
import com.example.Study_board.dao.MemberDao;
import com.example.Study_board.dto.BoardCreateReq;
import com.example.Study_board.dto.BoardListRes;
import com.example.Study_board.dto.BoardRes;
import com.example.Study_board.dto.SearchListRes;
import com.example.Study_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.io.IOException; // IOException 클래스 임포트

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final MemberDao memberDao;
    private final BoardDao boardDao;
    private static final String UPLOAD_DIR = "C:\\프로젝트\\image\\";
    private static final String WEB_ACCESS_PATH = "/image/";

    // 게시글 작성
    @Override
    public Long board(BoardCreateReq b, MultipartFile imageFile) {
        if (memberDao.findById(b.getMember_id()) == null) {
            throw new IllegalArgumentException("존재하지 않는 회원 입니다.");
        }
        // 1. 이미지 파일 처리 로직 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // 1-1. 고유한 파일 이름 생성
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String savedFilename = UUID.randomUUID().toString() + extension;

                // 1-2. 파일 저장 경로 설정 및 디렉토리 생성 확인
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // 디렉토리가 없으면 생성
                }

                // 1-3. 로컬 파일 시스템에 파일 저장
                File dest = new File(UPLOAD_DIR + savedFilename);
                imageFile.transferTo(dest); // 파일 저장 실행

                // 2. 생성된 웹 접근 URL 설정 (WebConfig의 /image/** 매핑 활용)
                String savedImageUrl = WEB_ACCESS_PATH + savedFilename;
                b.setImage_Url(savedImageUrl);
                log.info("이미지 첨부 성공: {}", savedImageUrl);

            } catch (IOException e) {
                // 파일 저장 중 IO 오류 발생 (권한, 디스크 공간 등)
                log.error("🚨 이미지 파일 저장 중 IOException 발생. 이미지 저장을 건너뜁니다.", e);
                // DTO에 image_Url을 설정하지 않고 null 상태로 유지하여 게시글은 작성되도록 함
                b.setImage_Url(null);
            } catch (Exception e) {
                // 그 외 오류
                log.error("🚨 이미지 파일 저장 중 알 수 없는 오류 발생.", e);
                b.setImage_Url(null);
            }
        } else {
            // 파일이 없거나 비어있는 경우 URL을 명시적으로 null로 설정 (BoardCreateReq 생성 시 null이면 생략 가능)
            b.setImage_Url(null);
        }


        // 3. DAO 호출
        return boardDao.save(b);
    }

    @Override
    public List<BoardListRes> list(String board) {
        return boardDao.findAllByBoardType(board);
    }

    // 게시글 수정
    @Override
    public boolean update(Long board_id, Long member_id, String title, String contents, String image_Url) {
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
            boolean update = boardDao.update(board_id, title, contents, image_Url);
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

    // 게시글 조회수 증가
    @Override
    public void increaseViewCount(Long id) {
        boardDao.increaseViewCount(id);
    }

    // 메인에 쓸 게시판글 조회
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

    // BOARD_ID로 게시글 조회
    @Override
    public BoardRes getboardRes(Long board_id) {
        return boardDao.findByBoardID(board_id);
    }

    @Override
    public boolean update(Long board_id, Long member_id, String title, String contents, MultipartFile newImageFile, boolean deleteImage) {
        // 1. 권한 및 게시글 존재 여부 확인
        Long writerId = boardDao.ByBoard_id(board_id);
        if (writerId == null || !Objects.equals(writerId, member_id)) {
            log.warn("수정 권한 없음 또는 게시글 부재 (board_id={}, 요청자={})", board_id, member_id);
            return false;
        }

        // 2. 현재 게시글 정보 조회 (기존 이미지 URL을 얻기 위해)
        BoardRes currentPost = boardDao.findByBoardID(board_id);
        String currentImageUrl = currentPost.getImageUrl(); // DB에 저장된 기존 이미지 URL
        String finalImageUrl = currentImageUrl; // 최종적으로 DB에 저장할 URL

        try {
            // 3. 이미지 처리 로직

            // 3-A. 기존 이미지 삭제 요청 처리 (`deleteImage` 체크박스 선택 시)
            if (deleteImage && currentImageUrl != null && !currentImageUrl.isEmpty()) {
                deleteLocalFile(currentImageUrl); // 로컬 파일 시스템에서 기존 파일 삭제
                finalImageUrl = null; // DB URL을 NULL로 설정
                currentImageUrl = null; // 현재 URL 상태 업데이트
            }

            // 3-B. 새로운 이미지 파일 업로드 처리
            if (newImageFile != null && !newImageFile.isEmpty()) {

                // 3-B-1. 새 파일 업로드 전, 기존 이미지 삭제 (단순 교체 상황)
                // (3-A에서 이미 삭제되지 않았고, 기존 이미지가 남아있다면 삭제)
                if (currentImageUrl != null) {
                    deleteLocalFile(currentImageUrl);
                }

                // 3-B-2. 새 파일 저장 및 URL 생성
                String originalFilename = newImageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String savedFilename = UUID.randomUUID().toString() + extension;

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 로컬 파일 시스템에 파일 저장
                File dest = new File(UPLOAD_DIR + savedFilename);
                newImageFile.transferTo(dest);

                // DB에 저장할 웹 접근 URL 생성
                finalImageUrl = WEB_ACCESS_PATH + savedFilename;
                log.info("게시글 수정 - 새 이미지 업로드 완료: {}", finalImageUrl);
            }

        } catch (IOException e) {
            log.error("🚨 게시글 수정 중 이미지 파일 처리 오류 발생 (board_id={}). 이미지 URL 변경 없이 진행합니다.", board_id, e);
            // 파일 처리 오류 시 finalImageUrl은 기존 값(currentImageUrl)을 유지하거나 null로 설정됨
            // 여기서는 기존 값을 유지하여 안전하게 게시글 수정만 완료하도록 처리
            finalImageUrl = currentImageUrl;
        } catch (Exception e) {
            log.error("🚨 게시글 수정 중 알 수 없는 오류 발생.", e);
            finalImageUrl = currentImageUrl;
        }

        // 4. DAO 호출: 수정된 제목, 내용, 그리고 최종 이미지 URL(finalImageUrl)을 전달
        boolean updated = boardDao.update(board_id, title, contents, finalImageUrl);

        if (updated) {
            log.info("✅ 게시글 수정 성공 (board_id={})", board_id);
        } else {
            log.warn("⚠️ 게시글 수정 실패: DB 반영 안됨 (board_id={})", board_id);
        }

        return updated;
    }
    // 기존 게시글 이미지 삭제
    private void deleteLocalFile(String imageUrl) throws IOException {
        if (imageUrl == null || !imageUrl.startsWith(WEB_ACCESS_PATH)) return;

        // 웹 접근 경로 (/image/)를 실제 파일 시스템 경로 (C:\...)로 변환
        String filename = imageUrl.substring(WEB_ACCESS_PATH.length());
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("로컬 이미지 파일 삭제 성공: {}", filePath);
        } else {
            log.warn("로컬 이미지 파일이 존재하지 않아 삭제를 건너뜁니다: {}", filePath);
        }
    }

}
