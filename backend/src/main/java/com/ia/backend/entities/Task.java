package com.ia.backend.entities;

import com.ia.backend.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Table(
        name = "tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "user_id"})
)
@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User assignedTo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}