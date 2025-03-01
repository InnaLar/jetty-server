package ru.larina.mapper;

import org.springframework.stereotype.Component;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;

@Component
public class TaskMapper {
    public Task taskCreationRequestToTask(final TaskCreationRq rq, final User user) {
        return Task.builder()
            .name(rq.getName())
            .user(user)
            .build();
    }

    public TaskCreationRs taskToTaskCreationResponse(final Task task) {
        return TaskCreationRs.builder()
            .id(task.getId())
            .nameTask(task.getName())
            .userId(task.getUser().getId())
            .build();
    }
}
