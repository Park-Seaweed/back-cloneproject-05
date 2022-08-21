package com.project.instagramcloneteam5.controller;

import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.ExceptionResponseDto;
import com.project.instagramcloneteam5.model.dto.CommitRequestDto;
import com.project.instagramcloneteam5.model.dto.CommitResponseDto;
import com.project.instagramcloneteam5.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommitController {
    private final CommitService commitService;

    // Commit 작성
    @PostMapping("/boards/comment/details/{commentId}")
    public ExceptionResponseDto boardCommit(@PathVariable(name="commentId") Long commentId, @RequestBody CommitRequestDto commitRequestDto) {
        CommitResponseDto commitResponseDto = commitService.boardCommit(commentId, commitRequestDto);
        return new ExceptionResponseDto(Code.OK, commitResponseDto);
    }



    // Commit 삭제
    // 유저 정보 추가
    @DeleteMapping("/boards/details/commit/{commitId}")
    public ExceptionResponseDto deleteComment(@PathVariable Long commitId) {
        commitService.deleteCommit(commitId);
        return new ExceptionResponseDto(Code.OK);
    }
}
