package ru.larina.repository;

import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.entity.Task;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.model.projections.TotalWorkByPeriodProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<TaskTimeShortSpentProjection> getUserTaskEffortsByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    List<TaskTimeLongSpentProjection> getUserWorkIntervalByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    TotalWorkByPeriodProjection getUserTotalWorkByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    void clearTaskTimes(Long userId);

    List<TaskTimeId> getEmptyTaskTimeByUser(Long userId);
}
