package com.ia.backend.services;

import com.ia.backend.dtos.TaskRequest;
import com.ia.backend.dtos.TaskResponse;
import com.ia.backend.dtos.UserResponse;
import com.ia.backend.entities.Task;
import com.ia.backend.entities.User;
import com.ia.backend.entities.enums.Role;
import com.ia.backend.entities.enums.Status;
import com.ia.backend.mappers.TaskMapper;
import com.ia.backend.repositories.TaskRepository;
import com.ia.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach; // Changed to BeforeEach for cleaner test isolation
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private User fakeMember;
    private Task fakeMemberTask;

    @BeforeEach
    public void setUp() {
        fakeMember = User.builder()
                .id(1L)
                .name("Test Member User")
                .email("test_member_user@gmail.com")
                .password("testMemberPassword")
                .build();

        fakeMemberTask = Task.builder()
                .id(1L)
                .title("Test Member Task")
                .description("This is a test member task")
                .assignedTo(fakeMember)
                .build();
    }

    @Test
    public void createTask_shouldCreateTaskSuccessfully() {
        // 1. ARRANGE
        TaskRequest request = new TaskRequest(
                "Test Member Task",
                "This is a test member task",
                1L);

        TaskResponse fakeResponse = new TaskResponse(
                1L,
                "Test Member Task",
                "This is a test member task",
                Status.TODO,
                new UserResponse(
                        1L,
                        "Test Member User",
                        "test_member_user@gmail.com",
                        Role.MEMBER
                ),
                LocalDateTime.now());

        when(userRepository.findByIdAndRole(any(), any())).thenReturn(Optional.of(fakeMember));
        when(taskRepository.existsByTitleAndAssignedTo_Id(any(), any())).thenReturn(false);
        when(taskRepository.save(any(Task.class))).thenReturn(fakeMemberTask);
        when(taskMapper.toDTO(any(Task.class))).thenReturn(fakeResponse);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result, "Task result should not be null");
        assertEquals(1L, result.id());
        assertEquals("Test Member Task", result.title());
        assertEquals("This is a test member task", result.description());
        assertEquals(Status.TODO, result.status());
        assertEquals(1L, result.assignedTo().id());
        assertEquals("Test Member User", result.assignedTo().name());
        assertEquals("test_member_user@gmail.com", result.assignedTo().email());
        assertEquals(Role.MEMBER, result.assignedTo().role());

        verify(taskRepository, times(1)).save(any(Task.class));
    }
}