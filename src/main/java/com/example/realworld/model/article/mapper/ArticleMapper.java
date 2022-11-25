package com.example.realworld.model.article.mapper;

import com.example.realworld.entity.Article;
import com.example.realworld.entity.User;
import com.example.realworld.model.article.dto.ArticleDTOCreate;
import com.example.realworld.model.article.dto.ArticleDTOResponse;
import com.example.realworld.model.article.dto.ArticleDTOUpdate;
import com.example.realworld.model.article.dto.AuthorDTOResponse;
import com.example.realworld.util.SlugUtil;

import java.util.Date;

public class ArticleMapper {
    public static Article toArticle(ArticleDTOCreate articleDTOCreate){
        Date now = new Date();
        Article article = Article.builder().slug(SlugUtil.getSlug(articleDTOCreate.getTitle()))
                .title(articleDTOCreate.getTitle()).description(articleDTOCreate.getDescription())
                .body(articleDTOCreate.getBody())
                .createdAt(now).updatedAt(now).build();
        article.setTagList(articleDTOCreate.getTagList());
        return article;
    }

    public static ArticleDTOResponse toArticleDTOResponse(Article article, boolean favorited, int favoritesCount, boolean isFollowing) {
        return ArticleDTOResponse.builder().slug(article.getSlug()).title(article.getTitle()).description(article.getDescription())
                .body(article.getBody()).tagList(article.getTagList())
                .createdAt(article.getCreatedAt()).updatedAt(article.getUpdatedAt()).favorited(favorited)
                .favoritesCount(favoritesCount).author(toArticleDTOResponse(article.getAuthor(), isFollowing)).build();
    }

    private static AuthorDTOResponse toArticleDTOResponse(User author, boolean isFollowing) {
        return AuthorDTOResponse.builder().username(author.getUsername()).bio(author.getBio())
                .image(author.getImage()).following(isFollowing).build();
    }

    public static void toArticle(Article article, ArticleDTOUpdate articleDTOUpdate){
        article.setTitle(articleDTOUpdate.getTitle());
        article.setSlug(SlugUtil.getSlug(articleDTOUpdate.getTitle()));
    }

}
