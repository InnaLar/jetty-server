package ru.larina.mapper;

import ru.larina.model.dto.taskDTO.TaskRegistrationRequest;
import ru.larina.model.dto.taskDTO.TaskRegistrationResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.service.UserService;

public class TaskMapper {
    public static Task taskRegistrationRequestToTask(TaskRegistrationRequest rq) {
        User user = new UserService().get(rq.getUserId());
        return Task.builder()
            .name(rq.getName())
            .user(user)
            .build();
    }

    public static TaskRegistrationResponse TaskToTaskRegistrationResponse(Task task) {
        return TaskRegistrationResponse.builder()
            .id(task.getId())
            .nameTask(task.getName())
            .userId(task.getUser().getId())
            .build();
    }
}
