package com.example.task.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    private LocalDate dueDate;

    @Column(nullable = false)
    private String ownerId; // This is the 'sub' from JWT

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
}

