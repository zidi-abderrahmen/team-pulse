package com.ia.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Table(name = "check_ins")
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class CheckIn {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String completedWork;

    @Column(nullable = false, length = 500)
    private String blockers;

    @Column(nullable = false, length = 500)
    private String nextSteps;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User submittedBy;
}