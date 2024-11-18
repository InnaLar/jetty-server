package ru.larina.model.dto.taskDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRegistrationResponse {
    private Long id;
    private String nameTask;
    private Long userId;
}
