package com.example.realworld.model.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTOUpdate {
    private String email;
    private String bio;
    private String image;
}
