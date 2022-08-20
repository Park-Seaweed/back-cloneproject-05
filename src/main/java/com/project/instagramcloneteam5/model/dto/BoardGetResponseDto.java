package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
public class BoardGetResponseDto {
    private Long boardId;
    private String username;
    private String content;
    private List<String> imgUrl;
    private String modifiedAt;
    private List<CommentResponseDto> commentList;

    public BoardGetResponseDto(Long boardId, Board board, List<String> imgUrl, List<CommentResponseDto> commentList) {
        this.boardId = boardId;
        this.username = board.getMember().getUsername();
        this.content = board.getContent();
        this.imgUrl = imgUrl;
        this.modifiedAt = formatter(board.getModifiedAt());
        this.commentList = commentList;
    }

    public String formatter(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime);
    }
}

