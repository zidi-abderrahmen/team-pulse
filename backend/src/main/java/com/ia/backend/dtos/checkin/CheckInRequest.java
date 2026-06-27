package com.ia.backend.dtos.checkin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CheckInRequest(

        @NotBlank(message = "Completed work cannot be blank")
        @Size(min = 5, max = 500, message = "Completed work must be between 5 and 500 characters")
        String completedWork,

        @NotBlank(message = "Blockers cannot be blank")
        @Size(min = 5, max = 500, message = "Blockers must be between 5 and 500 characters")
        String blockers,

        @NotBlank(message = "Next steps cannot be blank")
        @Size(min = 5, max = 500, message = "Next steps must be between 5 and 500 characters")
        String nextSteps
) {}