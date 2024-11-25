package ru.larina.repository;

import ru.larina.model.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
