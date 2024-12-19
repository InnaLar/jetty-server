package ru.larina.mapper;

import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.service.SecondToDuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportMapper {
    public TaskTimeShortSpent taskTimeShortSpentProjectionToTaskTimeShortSpent(TaskTimeShortSpentProjection projection) {
        return TaskTimeShortSpent.builder()
            .taskId(projection.getId())
            .timeSpent(SecondToDuration.getDurationfromSeconds(projection.getSumTime().longValue()))
            .build();
    }

    public TaskTimeLongSpent taskTimeLongSpentProjectionToTaskTimeLongSpent(TaskTimeLongSpentProjection taskTimeLongSpentProjection) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return TaskTimeLongSpent.builder()
            .id(taskTimeLongSpentProjection.getId())
            .startDateTime(LocalDateTime.parse(taskTimeLongSpentProjection.getStartDateTime(), formatter))
            .stopDateTime(LocalDateTime.parse(taskTimeLongSpentProjection.getStopDateTime(), formatter))
            .timeSpent(taskTimeLongSpentProjection.getTimeSpent())
            .build();
    }
}
