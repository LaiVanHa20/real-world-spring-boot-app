package com.example.realworld.exception.custom;

import com.example.realworld.model.CustomError;

public class CustomNotFoundException extends BaseCustomException {

    public CustomNotFoundException(CustomError customError) {
        super(customError);
    }

}
