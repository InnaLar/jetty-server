package ru.larina.mapper;

import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.entity.TaskTime;

public class TaskTimeMapper {
    public static TaskTimeResponse TaskTimeToTaskTimeResponse(TaskTime taskTime) {
        return TaskTimeResponse.builder()
            .taskTimeId(taskTime.getId())
            .taskId(taskTime.getTask().getId())
            .startDateTime(taskTime.getStartTime())
            .stopDateTime(taskTime.getStopTime())
            .build();
    }
}