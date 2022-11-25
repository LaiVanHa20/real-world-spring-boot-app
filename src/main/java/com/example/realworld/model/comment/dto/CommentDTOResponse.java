package com.example.realworld.model.comment.dto;

import com.example.realworld.model.article.dto.AuthorDTOResponse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOResponse {
    private Date createdAt;
    private Date updatedAt;
    private String body;
    private AuthorDTOResponse author;
}
