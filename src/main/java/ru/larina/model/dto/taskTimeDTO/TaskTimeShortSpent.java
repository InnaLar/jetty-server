package ru.larina.model.dto.taskTimeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
public class TaskTimeShortSpent {
    private Long taskId;
    private Duration timeSpent;

    public TaskTimeShortSpent(Long taskId, Duration timeSpent) {
        this.taskId = taskId;
        this.timeSpent = timeSpent;
    }
}
