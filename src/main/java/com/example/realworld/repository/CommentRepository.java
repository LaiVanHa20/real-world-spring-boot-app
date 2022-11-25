package com.example.realworld.repository;

import com.example.realworld.entity.Comment;
import com.example.realworld.model.comment.dto.CommentDTOResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findById(Integer id);

    List<Comment> findByArticleId(Integer articleId);
}
