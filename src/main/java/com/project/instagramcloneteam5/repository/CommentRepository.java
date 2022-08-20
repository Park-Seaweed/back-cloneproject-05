package com.project.instagramcloneteam5.repository;

import com.project.instagramcloneteam5.model.Board;
import com.project.instagramcloneteam5.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByBoard(Board boardId);

}
