package ru.larina.model.dto.userClearDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTimeDTO.TaskTimeId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskTimeClearResponse {
    private Long userId;
    private List<TaskTimeId> taskTimeIds;
}
