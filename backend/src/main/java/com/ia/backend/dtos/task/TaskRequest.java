package com.ia.backend.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Assigned to cannot be null")
        Long assignedTo
) {}