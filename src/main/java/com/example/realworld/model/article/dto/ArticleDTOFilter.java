package com.example.realworld.model.article.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTOFilter {
    private String tag;
    private String author;
    private String favorited;
    private int limit;
    private int offset;
}
