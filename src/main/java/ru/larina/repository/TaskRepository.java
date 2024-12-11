package ru.larina.repository;

import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.entity.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<TaskTimeShortSpent> getUserTaskEffortsByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    List<TaskTimeLongSpent> getUserWorkIntervalByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    Duration getUserTotalWorkByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

    void clearTaskTimes(Long userId);

    List<TaskTimeId> getEmptyTaskTimeByUser(Long userId);
}
