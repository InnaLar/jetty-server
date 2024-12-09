package ru.larina.repository;

import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<TaskTimeShortSpent> getUserTaskEfforts(Long userId, LocalDateTime startTime, LocalDateTime stopTime);
}
