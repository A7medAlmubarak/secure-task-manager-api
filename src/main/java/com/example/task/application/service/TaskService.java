package com.example.task.application.service;

import com.example.task.application.exception.task.TaskNotFoundException;
import com.example.task.application.exception.task.UnauthorizedAccessException;
import com.example.task.domain.entity.Task;
import com.example.task.infrastructure.database.dao.TaskRepository;
import com.example.task.presentation.dto.request.TaskRequestDTO;
import com.example.task.presentation.dto.response.TaskResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponseDTO createTask(TaskRequestDTO dto, String ownerId) {
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .dueDate(dto.getDueDate())
                .ownerId(ownerId)
                .build();

        return convertToDTO(taskRepository.save(task));
    }

    public Page<TaskResponseDTO> getAllTasks(String ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByOwnerId(ownerId, pageable)
                .map(this::convertToDTO);
    }
    public TaskResponseDTO getTaskById(Long id, String ownerId) {
        return taskRepository.findByIdAndOwnerId(id, ownerId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto, String ownerId) {
        Task task = taskRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new UnauthorizedAccessException("Not authorized"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());

        return convertToDTO(taskRepository.save(task));
    }

    public void deleteTask(Long id, String ownerId) {
        if (!taskRepository.existsByIdAndOwnerId(id, ownerId)) {
            throw new UnauthorizedAccessException("Not authorized");
        }
        taskRepository.deleteById(id);
    }

    private TaskResponseDTO convertToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        return dto;
    }

}