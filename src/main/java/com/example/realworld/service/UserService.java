package com.example.realworld.service;

import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.profile.dto.ProfileDTOResponse;
import com.example.realworld.model.user.dto.UserDTOCreate;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserService {

    public Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException;

    public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap);

    Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException;
}
