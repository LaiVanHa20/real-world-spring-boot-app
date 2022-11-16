package com.example.realworld.controller;

import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.realworld.model.profile.dto.ProfileDTOResponse;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles/{username}")
public class ProfileController {
    private final UserService userService; // Tieem UserService giong @Autowired

    @GetMapping("")
    public Map<String, ProfileDTOResponse> getProfile(@PathVariable String username) throws CustomNotFoundException {
        return userService.getProfile(username);
    }

    @PostMapping("/follow")
    public Map<String, ProfileDTOResponse> followUser(@PathVariable String username) throws CustomNotFoundException {
        return userService.followUser(username);
    }
    @DeleteMapping("/follow")
    public Map<String, ProfileDTOResponse> unfollowUser(@PathVariable String username) throws CustomNotFoundException {
        return userService.unfollowUser(username);
    }
}
