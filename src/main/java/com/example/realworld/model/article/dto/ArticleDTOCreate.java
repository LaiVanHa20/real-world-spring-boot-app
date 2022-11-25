package com.example.realworld.model.article.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTOCreate {
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
}
