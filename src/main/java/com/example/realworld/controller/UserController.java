package com.example.realworld.controller;

import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.user.dto.UserDTOCreate;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import com.example.realworld.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public Map<String, UserDTOResponse> login(@RequestBody Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException {
        return userService.authenticate(userLoginRequestMap);
    }

    @PostMapping("/users")
    public Map<String, UserDTOResponse> registerUser(
            @RequestBody Map<String, UserDTOCreate> userDTOCreateMap) {
        return userService.registerUser(userDTOCreateMap);
    }

    @GetMapping("/user")
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        return  userService.getCurrentUser();
    }
}
