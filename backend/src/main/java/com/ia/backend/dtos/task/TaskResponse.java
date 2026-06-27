package com.ia.backend.dtos.task;

import com.ia.backend.dtos.auth.UserResponse;
import com.ia.backend.entities.enums.Status;

import java.time.LocalDateTime;

public record TaskResponse(

        Long id,
        String title,
        String description,
        Status status,
        UserResponse assignedTo,
        LocalDateTime createdAt
) {}