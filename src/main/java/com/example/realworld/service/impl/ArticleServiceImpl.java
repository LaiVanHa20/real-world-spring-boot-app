package com.example.realworld.service.impl;

import com.example.realworld.entity.Article;
import com.example.realworld.entity.User;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.CustomError;
import com.example.realworld.model.article.dto.ArticleDTOCreate;
import com.example.realworld.model.article.dto.ArticleDTOFilter;
import com.example.realworld.model.article.dto.ArticleDTOResponse;
import com.example.realworld.model.article.dto.ArticleDTOUpdate;
import com.example.realworld.model.article.mapper.ArticleMapper;
import com.example.realworld.repository.ArticleRepository;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.repository.custom.ArticleCriteria;
import com.example.realworld.service.ArticleService;
import com.example.realworld.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleCriteria articleCriteria;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Map<String, ArticleDTOResponse> createArticle(Map<String, ArticleDTOCreate> articleDTOCreateMap) {
        ArticleDTOCreate articleDTOCreate = articleDTOCreateMap.get("article");
        Article article = ArticleMapper.toArticle(articleDTOCreate);

        User currentUser = userService.getUserLoggedIn();

        article.setAuthor(currentUser);
        article = articleRepository.save(article);

        Map<String, ArticleDTOResponse> wrapper = new HashMap<>();
        ArticleDTOResponse articleDTOResponse = ArticleMapper.toArticleDTOResponse(article, false, 0, checkFollowing(article.getSlug()));
        wrapper.put("article", articleDTOResponse);
        return wrapper;
    }

    @Override
    public Map<String, ArticleDTOResponse> getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug);
        Map<String, ArticleDTOResponse> wrapper = new HashMap<>();

        //CAN TINH TOAN them
        boolean favorited = checkFavorited(slug);
        int favoritesCount = article.getUserFavorited().size();
        ArticleDTOResponse articleDTOResponse = ArticleMapper.toArticleDTOResponse(article, favorited, favoritesCount, checkFollowing(slug));
        wrapper.put("article", articleDTOResponse);
        return wrapper;
    }

    @Override
    public Map<String, Object> getListArticles(ArticleDTOFilter filter) {
        Map<String, Object> results = articleCriteria.findAll(filter);
        List<Article> listArticles = (List<Article>) results.get("listArticles");
        long totalArticle = (long) results.get("totalArticle");


        List<ArticleDTOResponse> listArticleDTOResponses =
                listArticles.stream().map(article ->
                                ArticleMapper.toArticleDTOResponse(article, checkFavorited(article.getSlug()), article.getUserFavorited().size(), checkFollowing(article.getSlug())))
                        .collect(Collectors.toList());
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("articles", listArticleDTOResponses);
        wrapper.put("articlesCount", totalArticle);

        return wrapper;
    }

    @Override
    public void deleteArticleBySlug(String slug) throws CustomNotFoundException {
        Article article = articleRepository.findBySlug(slug);
        if (article != null){
            articleRepository.delete(article);
        }
        else throw new CustomNotFoundException(CustomError.builder().code("404").message("Article not found").build());
    }

    @Override
    public Map<String, ArticleDTOResponse> updateArticleBySlug(String slug, Map<String, ArticleDTOUpdate> articleDTOUpdateMap) throws CustomNotFoundException {
        Article article = articleRepository.findBySlug(slug);
        ArticleDTOUpdate articleDTOUpdate = articleDTOUpdateMap.get("article");
        if(article != null){
            ArticleMapper.toArticle(article, articleDTOUpdate);
            article = articleRepository.save(article);

            int favoritesCount = article.getUserFavorited().size();

            Map<String, ArticleDTOResponse> wrapper = new HashMap<>();
            ArticleDTOResponse articleDTOResponse = ArticleMapper.toArticleDTOResponse(article, checkFavorited(article.getSlug()), favoritesCount, checkFollowing(article.getSlug()));
            wrapper.put("article", articleDTOResponse);
            return wrapper;
        }
        else throw new CustomNotFoundException(CustomError.builder().code("404").message("Article not found").build());
    }

    @Override
    public Map<String, ArticleDTOResponse> favoriteArticle(String slug) {
        User userLoggedIn = userService.getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByEmail(userLoggedIn.getEmail());
        User user = userOptional.get();
        Article article = articleRepository.findBySlug(slug);
        boolean favorited = checkFavorited(slug);
        if (!favorited){
            favorited = true;
            user.getArticleFavorited().add(article);
            userRepository.save(user);
            article.getUserFavorited().add(userLoggedIn);
            articleRepository.save(article);
        }
        int favoritesCount = article.getUserFavorited().size();
        Map<String, ArticleDTOResponse> wrapper = new HashMap<>();
        ArticleDTOResponse articleDTOResponse = ArticleMapper.toArticleDTOResponse(article, favorited, favoritesCount, checkFollowing(slug));
        wrapper.put("article", articleDTOResponse);
        return wrapper;
    }

    @Override
    public Map<String, ArticleDTOResponse> unfavoriteArticle(String slug) {
        User userLoggedIn = userService.getUserLoggedIn();
        Article article = articleRepository.findBySlug(slug);
        Optional<User> userOptional = userRepository.findByEmail(userLoggedIn.getEmail());
        User user = userOptional.get();
        boolean favorited = checkFavorited(slug);
        if (favorited){
            favorited = false;
            user.getArticleFavorited().remove(article);
            userRepository.save(user);
            article.getUserFavorited().remove(userLoggedIn);
            articleRepository.save(article);
        }
        int favoritesCount = article.getUserFavorited().size();
        Map<String, ArticleDTOResponse> wrapper = new HashMap<>();
        ArticleDTOResponse articleDTOResponse = ArticleMapper.toArticleDTOResponse(article, favorited, favoritesCount, checkFollowing(slug));
        wrapper.put("article", articleDTOResponse);
        return wrapper;
    }

    @Override
    public Map<String, Object> getNewFeed(ArticleDTOFilter articleDTOFilter) {
        User currentUser = userService.getUserLoggedIn();
        Set<User> followings = currentUser.getFollowings();
        List<String> listUsername = followings.stream().map(u -> u.getUsername()).collect(Collectors.toList());

        Map<String, Object> articleMap = articleCriteria.getNewFeed(listUsername, articleDTOFilter);
        List<ArticleDTOResponse> articleDTOResponses = new ArrayList<>();
        List<Article> articles = (List<Article>) articleMap.get("listArticle");
        for (Article article : articles) {
            articleDTOResponses.add(ArticleMapper.toArticleDTOResponse(article, checkFavorited(article.getSlug()), article.getUserFavorited().size(), checkFollowing(article.getSlug())));
        }

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("article", articleDTOResponses);
        wrapper.put("articlesCount", (long) articleMap.get("articlesCount"));
        return wrapper;
    }
    public boolean checkFollowing(String slug){
        Article article = articleRepository.findBySlug(slug);
        User userLoggedIn = userService.getUserLoggedIn();
        User author = article.getAuthor();
        Set<User> followers = author.getFollowers();
        for(User u: followers){
            if(u.getId() == userLoggedIn.getId()){
                return true;
            }
        }
        return false;
    }

    private boolean checkFavorited(String slug){
        User userLoggedIn = userService.getUserLoggedIn();
        Article article = articleRepository.findBySlug(slug);
        Set<User> userFavorited = article.getUserFavorited();
        for (User u: userFavorited){
            if (u.getId() == userLoggedIn.getId()){
                return true;
            }
        }
        return false;
    }

}
