package com.example.realworld.model.user.mapper;

import com.example.realworld.entity.User;
import com.example.realworld.model.user.dto.UserDTOCreate;
import com.example.realworld.model.user.dto.UserDTOResponse;

public class UserMapper {
    public static UserDTOResponse toUserDTOResponse(User user) {
        return UserDTOResponse.builder().email(user.getEmail()).username(user.getUsername()).bio(user.getBio())
                .image(user.getImage()).build();
    }

    public static User toUser(UserDTOCreate userDTOCreate) {
        return User.builder().username(userDTOCreate.getUsername()).email(userDTOCreate.getEmail())
                .password(userDTOCreate.getPassword()).build();
    }
}
