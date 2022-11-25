package com.example.realworld.controller;

import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.article.dto.ArticleDTOCreate;
import com.example.realworld.model.article.dto.ArticleDTOFilter;
import com.example.realworld.model.article.dto.ArticleDTOResponse;
import com.example.realworld.model.article.dto.ArticleDTOUpdate;
import com.example.realworld.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @PostMapping("")
    public Map<String, ArticleDTOResponse> createArticle(@RequestBody Map<String, ArticleDTOCreate> articleDTOCreateMap){
        return articleService.createArticle(articleDTOCreateMap);
    }

    @GetMapping("/{slug}")
    public Map<String, ArticleDTOResponse> getArticleBySlug(@PathVariable String slug){
        return articleService.getArticleBySlug(slug);
    }

    @GetMapping("")
    public Map<String, Object> getListArticles(
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "favorited", required = false) String favorited,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset){
        ArticleDTOFilter filter = ArticleDTOFilter.builder().tag(tag).author(author).favorited(favorited).limit(limit).offset(offset).build();
        return articleService.getListArticles(filter);
    }

    // Tinh nang Chua phat trien
    @GetMapping("/feed")
    public Map<String, Object> newFeed(
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset) {
        ArticleDTOFilter articleDTOFilter = ArticleDTOFilter.builder()
                .limit(limit).offset(offset)
                .build();
        return articleService.getNewFeed(articleDTOFilter);
    }

    @PutMapping("/{slug}")
    public Map<String, ArticleDTOResponse> updateArticleBySlug(@PathVariable String slug, @RequestBody Map<String, ArticleDTOUpdate> articleDTOUpdateMap) throws CustomNotFoundException {
        return articleService.updateArticleBySlug(slug, articleDTOUpdateMap);
    }

    @DeleteMapping("/{slug}")
    public void deleteArticleBySlug(@PathVariable String slug) throws CustomNotFoundException {
        articleService.deleteArticleBySlug(slug);
    }

    @PostMapping("/{slug}/favorite")
    public Map<String, ArticleDTOResponse> favoriteArticle(@PathVariable String slug){
        return articleService.favoriteArticle(slug);
    }

    @DeleteMapping("/{slug}/favorite")
    public Map<String, ArticleDTOResponse> unfavoriteArticle(@PathVariable String slug){
        return articleService.unfavoriteArticle(slug);
    }
}
