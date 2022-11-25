package com.example.realworld.controller;

import com.example.realworld.model.tag.dto.TagDTOResponse;
import com.example.realworld.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("")
    public Map<String, List<String>> listOfTags(){
        return tagService.listOfTags();
    }
}
