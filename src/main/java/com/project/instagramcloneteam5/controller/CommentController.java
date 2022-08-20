package com.project.instagramcloneteam5.controller;


import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.ExceptionResponseDto;
import com.project.instagramcloneteam5.model.dto.CommentRequestDto;
import com.project.instagramcloneteam5.model.dto.CommentResponseDto;
import com.project.instagramcloneteam5.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Comment 작성
    @PostMapping("/boards/details/{boardId}")
    public ExceptionResponseDto postComment(@PathVariable(name="postId") Long boardId, @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.boardComment(boardId, commentRequestDto);
        return new ExceptionResponseDto(Code.OK, commentResponseDto);
    }

    // Comment 수정
    // 유저 정보 추가


    // Comment 삭제
    // 유저 정보 추가
    @DeleteMapping("/boards/details/comment/{commentId}")
    public ExceptionResponseDto deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ExceptionResponseDto(Code.OK);
    }
}