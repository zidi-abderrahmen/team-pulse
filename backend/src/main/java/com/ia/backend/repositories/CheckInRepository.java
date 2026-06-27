package com.ia.backend.repositories;

import com.ia.backend.entities.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    List<CheckIn> findAllByOrderBySubmittedAtDesc();

    List<CheckIn> findBySubmittedBy_IdOrderBySubmittedAtDesc(Long userId);

    List<CheckIn> findBySubmittedBy_Id(Long userId);

    boolean existsBySubmittedBy_IdAndSubmittedAtBetween(
            Long submittedById,
            LocalDateTime startOfWeek,
            LocalDateTime endOfWeek
    );
}