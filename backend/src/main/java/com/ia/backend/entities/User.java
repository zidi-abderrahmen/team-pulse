package com.ia.backend.entities;

import com.ia.backend.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString(exclude = "password") @Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;
}