package ru.larina.repository;

import ru.larina.model.entity.Task;

public interface TaskRepository {
    Task get(Long id);
    Task add(Task task);
    Task update(Task task);
}
