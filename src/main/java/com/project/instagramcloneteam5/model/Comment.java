package com.project.instagramcloneteam5.model;

import com.project.instagramcloneteam5.exception.advice.Code;
import com.project.instagramcloneteam5.exception.advice.PrivateException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany
    private List<Commit> commitList = new ArrayList<>();

    @OneToMany
    private List<Heart> heartsList = new ArrayList<>();

    public Comment(Board board, CommentRequestDto commentRequestDto, Member member) {
        if (!StringUtils.hasText(commentRequestDto.getComment())) {
            throw new PrivateException(Code.WRONG_INPUT_COMMENT);
        }
        this.board = board;
        this.comment = commentRequestDto.getComment();
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {
        if (!StringUtils.hasText(commentRequestDto.getComment())) {
            throw new PrivateException(Code.WRONG_INPUT_COMMENT);
        }
        this.comment = commentRequestDto.getComment();
    }

}
