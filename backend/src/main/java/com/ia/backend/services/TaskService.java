package com.ia.backend.services;

import com.ia.backend.dtos.TaskRequest;
import com.ia.backend.dtos.TaskResponse;
import com.ia.backend.entities.Task;
import com.ia.backend.entities.User;
import com.ia.backend.exceptions.AlreadyExistsException;
import com.ia.backend.exceptions.NotFoundException;
import com.ia.backend.mappers.TaskMapper;
import com.ia.backend.repositories.TaskRepository;
import com.ia.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return taskMapper.toDTO(taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id: " + id)));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User assignedUser = userRepository.findById(request.assignedTo())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.assignedTo()));

        if (taskRepository.existsByTitleAndAssignedTo_Id(request.title(), request.assignedTo())) {
            throw new AlreadyExistsException("Task already exists with title: " + request.title() + " and assigned to: " + request.assignedTo());
        }

        Task newTask = Task.builder()
                .title(request.title())
                .description(request.description())
                .assignedTo(assignedUser)
                .build();

        return taskMapper.toDTO(taskRepository.save(newTask));
    }
}