package com.example.realworld.service.impl;

import com.example.realworld.entity.Article;
import com.example.realworld.entity.Comment;
import com.example.realworld.entity.User;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.CustomError;
import com.example.realworld.model.article.dto.AuthorDTOResponse;
import com.example.realworld.model.comment.dto.CommentDTOCreate;
import com.example.realworld.model.comment.dto.CommentDTOResponse;
import com.example.realworld.model.comment.mapper.CommentMapper;
import com.example.realworld.repository.ArticleRepository;
import com.example.realworld.repository.CommentRepository;
import com.example.realworld.service.CommentService;
import com.example.realworld.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ArticleRepository articleRepository;
    private  final CommentRepository commentRepository;
    private final ArticleServiceImpl articleService;
    private final UserService userService;
    @Override
    public Map<String, CommentDTOResponse> addComment(String slug, Map<String, CommentDTOCreate> commentDTOCreateMap) {
        Article article = articleRepository.findBySlug(slug);
        CommentDTOCreate commentDTOCreate = commentDTOCreateMap.get("comment");
        Comment comment = CommentMapper.toComment(commentDTOCreate);
        User currentUser = userService.getUserLoggedIn();
        comment.setAuthor(currentUser);
        comment.setArticle(article);
        comment = commentRepository.save(comment);

        Map<String, CommentDTOResponse> wrapper = new HashMap<>();
        CommentDTOResponse commentDTOResponse = CommentMapper.toCommentDTOResponse(comment, articleService.checkFollowing(slug));
        wrapper.put("comment", commentDTOResponse);
        return wrapper;
    }

    @Override
    public List<CommentDTOResponse> getCommentsFromArticle(String slug) throws CustomNotFoundException {
        Article article = articleRepository.findBySlug(slug);
        List<Comment> commentList = commentRepository.findByArticleId(article.getId());

        return commentList.stream().map(comment -> {

            return CommentDTOResponse.builder()
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .body(comment.getBody())
                    .author(AuthorDTOResponse.builder()
                            .username(comment.getAuthor().getUsername())
                            .bio(comment.getAuthor().getBio())
                            .image(comment.getArticle().getAuthor().getImage())
                            .following(articleService.checkFollowing(slug))
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteCommFromArticle(String slug, Integer id) throws CustomNotFoundException {
        Optional<Comment>  commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            commentRepository.delete(comment);
        }
        else throw new CustomNotFoundException(CustomError.builder().code("404").message("Article not found").build());
    }
}
