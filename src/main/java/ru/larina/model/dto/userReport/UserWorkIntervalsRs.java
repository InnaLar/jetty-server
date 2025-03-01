package ru.larina.model.dto.userReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTime.TaskTimeLongSpent;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkIntervalsRs {
    private Long userId;
    private List<TaskTimeLongSpent> workIntervals;
}
