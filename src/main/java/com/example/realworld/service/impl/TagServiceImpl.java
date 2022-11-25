package com.example.realworld.service.impl;

import com.example.realworld.entity.Tag;
import com.example.realworld.model.tag.dto.TagDTOResponse;
import com.example.realworld.repository.TagRepository;
import com.example.realworld.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Map<String, List<String>> listOfTags() {
        Map<String, List<String>> wrapper = new HashMap<>();
        List<String> tagList = tagRepository.findAll().stream().map(Tag::getTag).distinct().collect(Collectors.toList());
        wrapper.put("tags", tagList);
        return wrapper;
    }
}
