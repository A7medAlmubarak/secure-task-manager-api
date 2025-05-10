package com.example.task.presentation.controller;

import com.example.task.application.service.TaskService;
import com.example.task.presentation.dto.request.TaskRequestDTO;
import com.example.task.presentation.dto.response.TaskResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto, Authentication auth) {
        String ownerId = auth.getName();
        TaskResponseDTO response = taskService.createTask(dto, ownerId);
        System.out.println("response"+response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String ownerId = auth.getName();
        Page<TaskResponseDTO> tasks = taskService.getAllTasks(ownerId, page, size);

        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id, Authentication auth) {
        String ownerId = auth.getName();
        return ResponseEntity.ok(taskService.getTaskById(id, ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto, Authentication auth) {
        String ownerId = auth.getName();
        return ResponseEntity.ok(taskService.updateTask(id, dto, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        String ownerId = auth.getName();
        taskService.deleteTask(id, ownerId);
        return ResponseEntity.ok().build();
    }
}