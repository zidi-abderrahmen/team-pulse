package com.ia.backend.dtos;

import com.ia.backend.entities.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(

        @NotNull(message = "Status cannot be null")
        Status status
) {}