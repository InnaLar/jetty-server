package ru.larina.repository;

import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;

import java.util.Optional;

public interface TaskTimeRepository extends CrudRepository<TaskTime, Long>{
    //todo Optional
    TaskTime findFirstByTaskIdOrderByIdDesc(Long taskId);
    void clear(TaskTime taskTime);
}
