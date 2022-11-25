package com.example.realworld.service;


import com.example.realworld.entity.Comment;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.comment.dto.CommentDTOCreate;
import com.example.realworld.model.comment.dto.CommentDTOResponse;

import java.util.List;
import java.util.Map;

public interface CommentService {
    Map<String, CommentDTOResponse> addComment(String slug, Map<String, CommentDTOCreate> commentDTOCreateMap);

    List<CommentDTOResponse> getCommentsFromArticle(String slug) throws CustomNotFoundException;

    void deleteCommFromArticle(String slug, Integer id) throws CustomNotFoundException;

}
