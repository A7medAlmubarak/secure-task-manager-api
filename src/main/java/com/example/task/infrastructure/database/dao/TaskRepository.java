package com.example.task.infrastructure.database.dao;

import com.example.task.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByOwnerId(String ownerId, Pageable pageable);
    Optional<Task> findByIdAndOwnerId(Long id, String ownerId);
    boolean existsByIdAndOwnerId(Long id, String ownerId);

}