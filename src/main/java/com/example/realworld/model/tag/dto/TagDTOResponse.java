package com.example.realworld.model.tag.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTOResponse {
    List<String> tags;
}
