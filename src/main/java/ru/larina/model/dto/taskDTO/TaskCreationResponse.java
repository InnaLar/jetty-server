package ru.larina.model.dto.taskDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationResponse {
    private Long id;
    private String nameTask;
    private Long userId;
}
