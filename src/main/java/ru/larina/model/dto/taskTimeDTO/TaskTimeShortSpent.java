package ru.larina.model.dto.taskTimeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskTimeShortSpent {
    private Long taskId;
    private Duration timeSpent;
}
