package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardUpdateResponseDto {

    private Long boardId;
    private String content;
    private String modifiedAt;

    public BoardUpdateResponseDto(Long boardId, Board board) {
        this.boardId = boardId;
        this.content = board.getContent();
        this.modifiedAt = formatter(board.getModifiedAt());
    }

    public String formatter(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime);
    }
}
