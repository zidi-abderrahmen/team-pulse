package com.ia.backend.dtos;

import com.ia.backend.entities.enums.Role;

public record UserResponse(

        Long id,
        String name,
        String email,
        Role role
) {}