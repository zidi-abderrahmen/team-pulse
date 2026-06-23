package com.ia.backend.repositories;

import com.ia.backend.entities.User;
import com.ia.backend.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByIdAndRole(Long id, Role role);

    Optional<User> findByRole(Role role);
}