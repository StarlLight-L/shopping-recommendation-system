package com.example.backend.model.dto;

import lombok.Data;

@Data
public class GoogleLoginDTO {

    private String username;

    private String password;

    private String email;

    private String avatar;

    // getter and setter
}
