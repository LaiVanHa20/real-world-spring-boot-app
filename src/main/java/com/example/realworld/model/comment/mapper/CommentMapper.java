package com.example.realworld.model.comment.mapper;


import com.example.realworld.entity.Comment;
import com.example.realworld.entity.User;
import com.example.realworld.model.article.dto.AuthorDTOResponse;
import com.example.realworld.model.comment.dto.CommentDTOCreate;
import com.example.realworld.model.comment.dto.CommentDTOResponse;
import lombok.Data;

import java.util.Date;

public class CommentMapper {
    public static Comment toComment(CommentDTOCreate commentDTOCreate){
        Date now = new Date();
        Comment comment = Comment.builder().createdAt(now).updatedAt(now).body(commentDTOCreate.getBody()).build();
        return comment;
    }

    public static CommentDTOResponse toCommentDTOResponse(Comment comment , boolean isFollowing){
        return CommentDTOResponse.builder().createdAt(comment.getCreatedAt()).updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(toArticleDTOResponse(comment.getAuthor(), isFollowing)).build();
    }

    private static AuthorDTOResponse toArticleDTOResponse(User author, boolean isFollowing) {
        return AuthorDTOResponse.builder().username(author.getUsername()).bio(author.getBio())
                .image(author.getImage()).following(isFollowing).build();
    }
}
