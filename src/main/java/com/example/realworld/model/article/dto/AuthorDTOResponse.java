package com.example.realworld.model.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTOResponse {
    private String username;
    private String bio;
    private String image;
    private boolean following;

}
