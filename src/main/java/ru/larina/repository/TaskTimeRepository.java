package ru.larina.repository;

import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;

public interface TaskTimeRepository {
    TaskTime get(Long id);
    TaskTime getLast(Long taskId);
    TaskTime add(TaskTime taskTime);
    TaskTime update(TaskTime taskTime);
    TaskTime clear(TaskTime taskTime);
}
