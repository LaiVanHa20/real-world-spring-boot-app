package com.example.realworld.service;

import com.example.realworld.entity.User;
import com.example.realworld.exception.custom.CustomBadRequestException;
import com.example.realworld.exception.custom.CustomNotFoundException;
import com.example.realworld.model.profile.dto.ProfileDTOResponse;
import com.example.realworld.model.user.dto.UserDTOCreate;
import com.example.realworld.model.user.dto.UserDTOLoginRequest;
import com.example.realworld.model.user.dto.UserDTOResponse;
import com.example.realworld.model.user.dto.UserDTOUpdate;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserService {

    Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException;
    Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap);

    Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException;

    Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException;

    User getUserLoggedIn();

    Map<String, UserDTOResponse> updateCurrentUser(Map<String, UserDTOUpdate> userDTOUpdateMap)
            throws CustomNotFoundException;
}
