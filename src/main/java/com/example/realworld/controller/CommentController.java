package com.example.realworld.controller;

import com.example.realworld.entity.Comment;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.comment.dto.CommentDTOCreate;
import com.example.realworld.model.comment.dto.CommentDTOResponse;
import com.example.realworld.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public Map<String, CommentDTOResponse> addComment(@PathVariable String slug, @RequestBody Map<String, CommentDTOCreate> commentDTOCreateMap){
        return commentService.addComment(slug, commentDTOCreateMap);
    }

    @GetMapping("")
    public List<CommentDTOResponse> getCommentsFromArticle(@PathVariable String slug) throws CustomNotFoundException {
        return commentService.getCommentsFromArticle(slug);
    }

    @DeleteMapping("/{id}")
    public void deleteCommFromArticle(@PathVariable String slug, @PathVariable Integer id) throws CustomNotFoundException {
        commentService.deleteCommFromArticle(slug, id);
    }


}
