package ru.larina.model.dto.userReportDTO;

import lombok.Builder;
import lombok.Data;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;

import java.util.List;

@Data
@Builder
public class UserWorkIntervalsResponse {
    private Integer userId;
    private List<TaskTimeLongSpent> workIntervals;
}
