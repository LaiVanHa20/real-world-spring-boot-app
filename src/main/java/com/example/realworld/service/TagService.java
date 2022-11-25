package com.example.realworld.service;

import com.example.realworld.model.tag.dto.TagDTOResponse;

import java.util.List;
import java.util.Map;

public interface TagService {
    Map<String, List<String>> listOfTags();
}
