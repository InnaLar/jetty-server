package ru.larina.repository;

import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;

public interface TaskTimeRepository extends CrudRepository<TaskTime, Long>{
    TaskTime findFirstByTaskIdOrderByIdDesc(Long taskId);
    void clear(TaskTime taskTime);
}
