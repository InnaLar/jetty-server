package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.mapper.ReportMapper;
import ru.larina.model.dto.taskTime.TaskTimeLongSpent;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.dto.userReport.UserTaskEffortRs;
import ru.larina.model.dto.userReport.UserTotalWorkByPeriodRs;
import ru.larina.model.dto.userReport.UserWorkIntervalsRs;
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

    public UserTaskEffortRs getUserTaskEffortByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final List<TaskTimeShortSpentProjection> taskTimeShortSpentProjections = taskRepository.getUserTaskEffortsByPeriods(userId, startTime, stopTime);
        final List<TaskTimeShortSpent> taskTimeShortSpents = taskTimeShortSpentProjections.stream()
            .map(new ReportMapper()::taskTimeShortSpentProjectionToTaskTimeShortSpent)
            .toList();
        return UserTaskEffortRs.builder()
            .userId(userId)
            .taskEfforts(taskTimeShortSpents)
            .build();
    }

    public UserWorkIntervalsRs getUserWorkIntervalByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final List<TaskTimeLongSpentProjection> projections = taskRepository.getUserWorkIntervalByPeriods(userId, startTime, stopTime);
        final List<TaskTimeLongSpent> taskTimeLongSpents = projections.stream()
            .map(new ReportMapper()::taskTimeLongSpentProjectionToTaskTimeLongSpent)
            .toList();
        return UserWorkIntervalsRs.builder()
            .userId(userId)
            .workIntervals(taskTimeLongSpents)
            .build();
    }

    public UserTotalWorkByPeriodRs getUserTotalWorkByPeriod(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final TotalWorkByPeriodProjection timeSpent = taskRepository.getUserTotalWorkByPeriods(userId, startTime, stopTime);
        final BigDecimal totalTime = timeSpent.getTotalTime();
        Duration duration = null;
        if (totalTime != null) {
//            duration = SecondToDuration.getDurationfromSeconds(totalTime.longValue());
              duration = Duration.ofNanos(totalTime.longValue());
        }
        return UserTotalWorkByPeriodRs.builder()
            .userId(userId)
            .totalWork(duration)
            .build();
    }

}
