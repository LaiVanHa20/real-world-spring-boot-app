package com.example.realworld.model.user.mapper;

import com.example.realworld.entity.User;
import com.example.realworld.model.user.dto.UserDTOResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDTOResponse() {
        //given
        User user = User.builder().email("neo@gmail.com").username("neo").bio("bio").image("image").build();
        UserDTOResponse expect = UserDTOResponse.builder().email("neo@gmail.com").username("neo").bio("bio").image("image").build();
        //when
        UserDTOResponse actual = UserMapper.toUserDTOResponse(user);
        //then
        assertEquals(expect, actual);
    }

    @Test
    void toUser() {
    }
}