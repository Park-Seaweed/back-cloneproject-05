package com.project.instagramcloneteam5.model.dto;

import com.project.instagramcloneteam5.model.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Getter
public class CommentResponseDto {
    private Long commentId;
    private String username;
    private String comment;
    private String modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.modifiedAt = formatter(comment.getModifiedAt());
    }

    public String formatter(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime);
    }
}
