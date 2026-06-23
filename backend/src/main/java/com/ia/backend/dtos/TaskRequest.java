package com.ia.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotBlank(message = "Assigned to cannot be blank")
        Long assignedTo
) {}