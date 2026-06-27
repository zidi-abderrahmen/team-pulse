package com.ia.backend.services;

import com.ia.backend.dtos.task.TaskRequest;
import com.ia.backend.dtos.task.TaskResponse;
import com.ia.backend.dtos.task.UpdateTaskRequest;
import com.ia.backend.entities.Task;
import com.ia.backend.entities.User;
import com.ia.backend.entities.enums.Role;
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
    public List<TaskResponse> getTasksByAssignedTo(User currentUser) {
        return taskRepository.findByAssignedTo_Id(currentUser.getId()).stream().map(taskMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, User currentUser) {
        if (currentUser.getRole() == Role.MEMBER) {
            return taskMapper.toDTO(taskRepository.findByAssignedTo_IdAndId(currentUser.getId(), id)
                    .orElseThrow(() -> new NotFoundException("Task not found with id: '" + id + "' and assigned to member with id: '" + currentUser.getId() + "'")));
        }
        return taskMapper.toDTO(taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id: " + id)));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        User assignedUser = userRepository.findByIdAndRole(request.assignedTo(), Role.MEMBER)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + request.assignedTo()));

        if (taskRepository.existsByTitleAndAssignedTo_Id(request.title(), request.assignedTo())) {
            throw new AlreadyExistsException("Task already exists with title: '" + request.title() + "' and assigned to member with id: '" + request.assignedTo() + "'");
        }

        Task newTask = Task.builder()
                .title(request.title())
                .description(request.description())
                .assignedTo(assignedUser)
                .build();

        return taskMapper.toDTO(taskRepository.save(newTask));
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, User currentUser, UpdateTaskRequest request) {
        Task existingTask = taskRepository.findByAssignedTo_IdAndId(currentUser.getId(), taskId)
                .orElseThrow(() -> new NotFoundException("Task not found with id: '" + taskId + "' and assigned to member with id: '" + currentUser.getId() + "'"));

        existingTask.setStatus(request.status());

        return taskMapper.toDTO(taskRepository.save(existingTask));
    }
}