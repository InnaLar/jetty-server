package ru.larina.mapper;

import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.dto.userClearDTO.UserDeleteTasksResponse;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public User userRegistrationRequestToUser(final UserRegistrationRequest rq) {
        return User.builder()
            .email(rq.getEmail())
            .build();
    }

    public User userPutRequestToUser(final UserPutRequest rq) {
        return User.builder()
            .id(rq.getId())
            .email(rq.getEmail())
            .build();
    }

    public UserRegistrationResponse userToUserRegistrationResponse(final User user) {
        return UserRegistrationResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserPutResponse userToUserPutResponse(final User user) {
        return UserPutResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserTaskTimeClearResponse userToUserTaskTimeClearResponse(final User user, List<TaskTimeId> taskTimeIds) {
        return UserTaskTimeClearResponse.builder()
            .userId(user.getId())
            .taskTimeIds(taskTimeIds)
            .build();
    }

    public UserTaskEffortResponse userToUserTaskEffortResponse(final User user) {
        final List<TaskTimeShortSpent> taskTimeShortSpents = new ArrayList<>();
        for (Task task : user.getTasks()) {
            for (TaskTime taskTime : task.getTaskTimes()) {
                taskTimeShortSpents.add(TaskTimeShortSpent.builder()
                    .taskId(taskTime.getTask().getId())
                    .timeSpent(Duration.between(taskTime.getStartTime(), taskTime.getStopTime()))
                    .build());
            }
        }
        return UserTaskEffortResponse.builder()
            .userId(user.getId())
            .taskEfforts(taskTimeShortSpents)
            .build();
    }

    public UserDeleteTasksResponse userToUserDeleteTasksResponse(final User user) {
        return UserDeleteTasksResponse.builder()
            .userId(user.getId())
            .build();
    }
}
