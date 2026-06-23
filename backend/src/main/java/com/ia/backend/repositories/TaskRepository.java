package com.ia.backend.repositories;

import com.ia.backend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);
    boolean existsByTitle(String title);
}