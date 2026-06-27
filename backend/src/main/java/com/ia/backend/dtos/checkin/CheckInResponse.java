package com.ia.backend.dtos.checkin;

import com.ia.backend.dtos.auth.UserResponse;

import java.time.LocalDateTime;

public record CheckInResponse(

        Long id,
        String completedWork,
        String blockers,
        String nextSteps,
        LocalDateTime submittedAt,
        UserResponse submittedBy
) {}