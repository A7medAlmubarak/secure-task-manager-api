package com.example.task.presentation.dto.request;

import com.example.task.domain.entity.Task;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Task.Status status;
    @FutureOrPresent(message = "Due date must be today or a future date")
    private LocalDate dueDate;

}