package ru.larina.model.dto.userReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkIntervalsResponse {
    private Long userId;
    private List<TaskTimeLongSpent> workIntervals;
}
