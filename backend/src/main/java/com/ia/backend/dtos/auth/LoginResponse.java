package com.ia.backend.dtos.auth;

public record LoginResponse(

        String token,
        UserResponse user
) {}