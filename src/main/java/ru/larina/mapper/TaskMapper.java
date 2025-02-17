package ru.larina.mapper;

import ru.larina.model.dto.task.TaskCreationRequest;
import ru.larina.model.dto.task.TaskCreationResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;

public class TaskMapper {
    public Task taskCreationRequestToTask(final TaskCreationRequest rq, final User user) {
        return Task.builder()
            .name(rq.getName())
            .user(user)
            .build();
    }

    public TaskCreationResponse taskToTaskCreationResponse(final Task task) {
        return TaskCreationResponse.builder()
            .id(task.getId())
            .nameTask(task.getName())
            .userId(task.getUser().getId())
            .build();
    }
}
