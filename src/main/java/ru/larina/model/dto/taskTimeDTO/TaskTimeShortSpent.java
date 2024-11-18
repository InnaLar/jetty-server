package ru.larina.model.dto.taskTimeDTO;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class TaskTimeShortSpent {
    private Integer taskId;
    private Duration timeSpent;
}
