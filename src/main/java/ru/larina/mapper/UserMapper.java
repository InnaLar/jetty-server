package ru.larina.mapper;

import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User UserRegistrationRequestToUser(UserRegistrationRequest rq) {
        return User.builder()
            .email(rq.getEmail())
            .build();
    }

    public static User UserPutRequestToUser(UserPutRequest rq) {
        return User.builder()
            .id(rq.getId())
            .email(rq.getEmail())
            .build();
    }

    public static UserRegistrationResponse UserToUserRegistrationResponse(User user) {
        return UserRegistrationResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public static UserPutResponse UserToUserPutResponse(User user) {
        return UserPutResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public static UserTaskTimeClearResponse UserToUserTaskTimeClearResponse(User user) {
        List<TaskTimeId> timeIds = new ArrayList<>();
        for(Task task: user.getTasks())
            for(TaskTime taskTime: task.getTaskTimes()) {
                timeIds.add(TaskTimeId.builder()
                        .id(taskTime.getId())
                    .build());
            }
        return UserTaskTimeClearResponse.builder()
            .userId(user.getId())
            .taskTimeIds(timeIds)
            .build();
    }
}
