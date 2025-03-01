package ru.larina.mapper;

import ru.larina.model.dto.taskTime.TaskTimeLongSpent;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.service.SecondToDuration;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportMapper {
    public TaskTimeShortSpent taskTimeShortSpentProjectionToTaskTimeShortSpent(final TaskTimeShortSpentProjection projection) {
        return TaskTimeShortSpent.builder()
            .taskId(projection.getId())
            .timeSpent(Duration.ofNanos(projection.getSumTime().longValue()))
            .build();
    }

    public TaskTimeLongSpent taskTimeLongSpentProjectionToTaskTimeLongSpent(final TaskTimeLongSpentProjection taskTimeLongSpentProjection) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return TaskTimeLongSpent.builder()
            .id(taskTimeLongSpentProjection.getId())
            .startDateTime(LocalDateTime.parse(taskTimeLongSpentProjection.getStartDateTime(), formatter))
            .stopDateTime(LocalDateTime.parse(taskTimeLongSpentProjection.getStopDateTime(), formatter))
            .timeSpent(taskTimeLongSpentProjection.getTimeSpent())
            .build();
    }
}
