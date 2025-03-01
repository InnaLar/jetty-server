package ru.larina.model.dto.userClear;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larina.model.dto.taskTime.TaskTimeId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskTimeClearRs {
    private Long userId;
    private List<Long> taskTimeIds;
}
