package com.ia.backend.controllers;

import com.ia.backend.configs.SecurityUtils;
import com.ia.backend.dtos.checkin.CheckInRequest;
import com.ia.backend.dtos.checkin.CheckInResponse;
import com.ia.backend.entities.User;
import com.ia.backend.services.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<CheckInResponse>> getAllCheckIns() {
        return ResponseEntity.ok(checkInService.getAllCheckIns());
    }

    @GetMapping("/member/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<CheckInResponse>> getAllCheckInsOfMember(@PathVariable Long id) {
        return ResponseEntity.ok(checkInService.getAllCheckInsOfMember(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<CheckInResponse> createCheckIn(@Valid @RequestBody CheckInRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(checkInService.createCheckIn(currentUser, request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<CheckInResponse>> getMyCheckIns() {
        User currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(checkInService.getMyCheckIns(currentUser));
    }
}