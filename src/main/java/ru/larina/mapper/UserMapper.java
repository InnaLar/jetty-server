package ru.larina.mapper;

import org.springframework.stereotype.Component;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.dto.userClear.UserDeleteTasksResponse;
import ru.larina.model.dto.userClear.UserTaskTimeClearResponse;
import ru.larina.model.dto.user.UserPutRequest;
import ru.larina.model.dto.user.UserPutResponse;
import ru.larina.model.dto.user.UserRegistrationRequest;
import ru.larina.model.dto.user.UserRegistrationResponse;
import ru.larina.model.dto.userReport.UserTaskEffortResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
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

    public UserTaskTimeClearResponse userToUserTaskTimeClearResponse(final User user, final List<TaskTimeId> taskTimeIds) {
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
