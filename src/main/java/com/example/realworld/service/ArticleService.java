package com.example.realworld.service;

import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.article.dto.ArticleDTOCreate;
import com.example.realworld.model.article.dto.ArticleDTOFilter;
import com.example.realworld.model.article.dto.ArticleDTOResponse;
import com.example.realworld.model.article.dto.ArticleDTOUpdate;

import java.util.Map;

public interface ArticleService {
    Map<String, ArticleDTOResponse> createArticle(Map<String, ArticleDTOCreate> articleDTOCreateMap);

    Map<String, ArticleDTOResponse> getArticleBySlug(String slug);

    Map<String, Object> getListArticles(ArticleDTOFilter filter);

    void deleteArticleBySlug(String slug) throws CustomNotFoundException;

    Map<String, ArticleDTOResponse> updateArticleBySlug(String slug, Map<String, ArticleDTOUpdate> articleDTOUpdateMap) throws CustomNotFoundException;

    Map<String, ArticleDTOResponse> favoriteArticle(String slug);

    Map<String, ArticleDTOResponse> unfavoriteArticle(String slug);

    Map<String, Object> getNewFeed(ArticleDTOFilter articleDTOFilter);
}
