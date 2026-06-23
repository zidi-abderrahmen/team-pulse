package com.ia.backend.controllers;

import com.ia.backend.configs.SecurityUtils;
import com.ia.backend.dtos.TaskRequest;
import com.ia.backend.dtos.TaskResponse;
import com.ia.backend.dtos.UpdateTaskRequest;
import com.ia.backend.entities.User;
import com.ia.backend.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MEMBER')")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignedTo() {
        User currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(taskService.getTasksByAssignedTo(currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(taskService.updateTask(id, currentUser, request));
    }
}