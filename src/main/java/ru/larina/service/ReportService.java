package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.mapper.ReportMapper;
import ru.larina.model.dto.taskTime.TaskTimeLongSpent;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.dto.userReport.UserTaskEffortResponse;
import ru.larina.model.dto.userReport.UserTotalWorkByPeriodResponse;
import ru.larina.model.dto.userReport.UserWorkIntervalsResponse;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.model.projections.TotalWorkByPeriodProjection;
import ru.larina.repository.TaskRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class ReportService {
    private final TaskRepository taskRepository;

    public UserTaskEffortResponse getUserTaskEffortByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final List<TaskTimeShortSpentProjection> taskTimeShortSpentProjections = taskRepository.getUserTaskEffortsByPeriods(userId, startTime, stopTime);
        final List<TaskTimeShortSpent> taskTimeShortSpents = taskTimeShortSpentProjections.stream()
            .map(new ReportMapper()::taskTimeShortSpentProjectionToTaskTimeShortSpent)
            .toList();
        return UserTaskEffortResponse.builder()
            .userId(userId)
            .taskEfforts(taskTimeShortSpents)
            .build();
    }

    public UserWorkIntervalsResponse getUserWorkIntervalByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final List<TaskTimeLongSpentProjection> projections = taskRepository.getUserWorkIntervalByPeriods(userId, startTime, stopTime);
        final List<TaskTimeLongSpent> taskTimeLongSpents = projections.stream()
            .map(new ReportMapper()::taskTimeLongSpentProjectionToTaskTimeLongSpent)
            .toList();
        return UserWorkIntervalsResponse.builder()
            .userId(userId)
            .workIntervals(taskTimeLongSpents)
            .build();
    }

    public UserTotalWorkByPeriodResponse getUserTotalWorkByPeriod(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final TotalWorkByPeriodProjection timeSpent = taskRepository.getUserTotalWorkByPeriods(userId, startTime, stopTime);
        final BigDecimal totalTime = timeSpent.getTotalTime();
        Duration duration = null;
        if (totalTime != null) {
            duration = SecondToDuration.getDurationfromSeconds(totalTime.longValue());
        }
        return UserTotalWorkByPeriodResponse.builder()
            .userId(userId)
            .totalWork(duration)
            .build();
    }

}
