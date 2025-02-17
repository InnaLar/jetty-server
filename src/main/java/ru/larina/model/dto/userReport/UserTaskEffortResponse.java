package ru.larina.model.dto.userReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskEffortResponse {
    private Long userId;
    private List<TaskTimeShortSpent> taskEfforts;
}
