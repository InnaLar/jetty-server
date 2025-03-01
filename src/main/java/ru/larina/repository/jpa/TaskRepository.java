package ru.larina.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
