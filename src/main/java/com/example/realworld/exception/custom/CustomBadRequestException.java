package com.example.realworld.exception.custom;

import com.example.realworld.model.CustomError;

public class CustomBadRequestException extends BaseCustomException {

    public CustomBadRequestException(CustomError customError) {
        super(customError);
    }

}
