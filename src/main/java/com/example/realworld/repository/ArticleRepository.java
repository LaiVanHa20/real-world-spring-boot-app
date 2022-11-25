package com.example.realworld.repository;
import com.example.realworld.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Article findBySlug(String slug);
}
