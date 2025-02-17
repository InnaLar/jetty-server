package ru.larina.repository;

import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.entity.TaskTime;

import java.util.List;
import java.util.Optional;

public interface TaskTimeRepository extends CrudRepository<TaskTime, Long> {
    Optional<TaskTime> findFirstByTaskIdOrderByIdDesc(Long taskId);

    void clear(TaskTime taskTime);

    void clearByUser(Long userId);

    List<TaskTimeId> getTaskTimesByUser(Long userId);
}

