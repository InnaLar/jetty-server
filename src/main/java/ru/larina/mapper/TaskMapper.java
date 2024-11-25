package ru.larina.mapper;

import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.repository.impl.UserRepositoryImpl;

public class TaskMapper {
    public static Task taskCreationRequestToTask(TaskCreationRequest rq, User user) {
        return Task.builder()
            .name(rq.getName())
            .user(user)
            .build();
    }

    public static TaskCreationResponse taskToTaskCreationResponse(Task task) {
        return TaskCreationResponse.builder()
            .id(task.getId())
            .nameTask(task.getName())
            .userId(task.getUser().getId())
            .build();
    }
}
