package ru.larina.mapper;

import org.springframework.stereotype.Component;
import ru.larina.model.dto.taskTime.TaskTimeRs;
import ru.larina.model.entity.TaskTime;

@Component
public class TaskTimeMapper {
    public TaskTimeRs taskTimeToTaskTimeResponse(final TaskTime taskTime) {
        return TaskTimeRs.builder()
            .taskTimeId(taskTime.getId())
            .taskId(taskTime.getTask().getId())
            .startDateTime(taskTime.getStartTime())
            .stopDateTime(taskTime.getStopTime())
            .build();
    }

}
