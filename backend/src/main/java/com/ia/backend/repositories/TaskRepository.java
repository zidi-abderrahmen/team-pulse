package com.ia.backend.repositories;

import com.ia.backend.entities.Task;
import com.ia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitleAndAssignedTo(String title, User assignedTo);
    boolean existsByTitleAndAssignedTo_Id(String title, Long assignedToId);

    List<Task> findByAssignedTo_Id(Long assignedToId);

    Optional<Task> findByAssignedTo_IdAndId(Long assignedToId, Long id);
}