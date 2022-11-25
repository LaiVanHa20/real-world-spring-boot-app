package com.example.realworld.service.impl;

import com.example.realworld.entity.User;
import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  JwtTokenUtil jwtTokenUtil;
    @Mock
    private  PasswordEncoder passwordEncoder;

    @Test
    void authenticate_success() throws CustomBadRequestException {
        //given
        UserDTOLoginRequest userDTOLoginRequest = UserDTOLoginRequest.builder().email("email").password("123").build();
        Map<String, UserDTOLoginRequest> userLoginRequestMap = new HashMap<>();
        userLoginRequestMap.put("user", userDTOLoginRequest);

        Optional<User> userOptional = Optional.of(User.builder().id(1).email("email").password("123").username("username").build());

        Map<String, UserDTOResponse> expected = new HashMap<>();
        UserDTOResponse userDTOResponseExpected = UserDTOResponse.builder().email("email").username("username").token("TOKEN").build();
        expected.put("user", userDTOResponseExpected);

        //when
        when(userRepository.findByEmail(userDTOLoginRequest.getEmail())).thenReturn(userOptional);
        when(passwordEncoder.matches("123", "123")).thenReturn(true);
        when(jwtTokenUtil.generateToken(userOptional.get(), 24*60*60)).thenReturn("TOKEN");

        Map<String, UserDTOResponse> actual = userService.authenticate(userLoginRequestMap);
        //then
        assertEquals(true, actual.containsKey("user"));
        UserDTOResponse userDTOResponseActual = actual.get("user");
        assertEquals(userDTOResponseExpected.getUsername(), userDTOResponseActual.getUsername());
        assertEquals(userDTOResponseExpected.getEmail(), userDTOResponseActual.getEmail());
        assertEquals(userDTOResponseExpected.getToken(), userDTOResponseActual.getToken());
    }
    @Test
    void authenticate_throw_Ex() throws CustomBadRequestException {
        //given
        UserDTOLoginRequest userDTOLoginRequest = UserDTOLoginRequest.builder().email("email").password("123").build();
        Map<String, UserDTOLoginRequest> userLoginRequestMap = new HashMap<>();
        userLoginRequestMap.put("user", userDTOLoginRequest);

        Optional<User> userOptional = Optional.of(User.builder().id(1).email("email").password("123").username("username").build());

        Map<String, UserDTOResponse> expected = new HashMap<>();
        UserDTOResponse userDTOResponseExpected = UserDTOResponse.builder().email("email").username("username").token("TOKEN").build();
        expected.put("user", userDTOResponseExpected);

        //when
        when(userRepository.findByEmail(userDTOLoginRequest.getEmail())).thenReturn(Optional.ofNullable(null));
        //when(passwordEncoder.matches("123", "123")).thenReturn(true);
        //when(jwtTokenUtil.generateToken(any(User.class), 24*60*60)).thenReturn("TOKEN");

        //then
        assertThrows(CustomBadRequestException.class, ()-> userService.authenticate(userLoginRequestMap));
    }

    @Test
    void registerUser() {
    }

    @Test
    void getCurrentUser() {
    }

    @Test
    void getProfile() {
    }

    @Test
    void followUser() {
    }

    @Test
    void unfollowUser() {
    }

    @Test
    void getUserLoggedIn() {
    }
}