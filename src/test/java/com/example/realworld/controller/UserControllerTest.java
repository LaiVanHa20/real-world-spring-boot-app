package com.example.realworld.controller;

import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import com.example.realworld.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    //Dung MockMVC test controller tra ve JSON
    @Test
    void login() throws CustomBadRequestException {
        //given
        Map<String, UserDTOResponse> userDTOResponseMap = new HashMap<>();
        Map<String, UserDTOLoginRequest> userDTOLoginRequestMap = new HashMap<>();
        userDTOResponseMap.put("user", UserDTOResponse.builder().build());
        //when
        when(userService.authenticate(userDTOLoginRequestMap)).thenReturn(userDTOResponseMap);
        Map<String, UserDTOResponse> actual = userController.login(userDTOLoginRequestMap);
        //the

        assertEquals(true, actual.containsKey("user"));

    }

    @Test
    void registerUser() {
    }

    @Test
    void getCurrentUser() {
    }
}