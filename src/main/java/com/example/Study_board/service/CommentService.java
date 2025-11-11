package com.example.Study_board.service;

import com.example.Study_board.dto.CommentCreateReq;

public interface CommentService {
    Long write(CommentCreateReq req);
}
