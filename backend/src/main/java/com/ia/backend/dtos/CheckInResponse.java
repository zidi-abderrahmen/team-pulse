package com.ia.backend.dtos;

import java.time.LocalDateTime;

public record CheckInResponse(

        Long id,
        String completedWork,
        String blockers,
        String nextSteps,
        LocalDateTime submittedAt,
        UserResponse submittedBy
) {}