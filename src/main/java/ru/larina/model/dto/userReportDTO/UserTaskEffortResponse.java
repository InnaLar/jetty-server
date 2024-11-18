package ru.larina.model.dto.userReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskEffortResponse {
    private Integer userId;
    private List<TaskTimeShortSpent> taskEfforts;
}
