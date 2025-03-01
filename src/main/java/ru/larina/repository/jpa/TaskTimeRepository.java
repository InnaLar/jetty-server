package ru.larina.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.entity.TaskTime;

import java.util.List;
import java.util.Optional;

public interface TaskTimeRepository extends JpaRepository<TaskTime, Long> {
    @Query(value = """
        SELECT *
        FROM task_time tt
        WHERE tt.task_id = ?
        ORDER BY tt.start_time DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<TaskTime> findFirstByTaskIdOrderByIdDesc(Long taskId);
}
