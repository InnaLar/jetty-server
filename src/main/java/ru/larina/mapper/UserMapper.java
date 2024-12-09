package ru.larina.mapper;

import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
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
    public User UserRegistrationRequestToUser(UserRegistrationRequest rq) {
        return User.builder()
            .email(rq.getEmail())
            .build();
    }

    public User userPutRequestToUser(UserPutRequest rq) {
        return User.builder()
            .id(rq.getId())
            .email(rq.getEmail())
            .build();
    }

    public UserRegistrationResponse userToUserRegistrationResponse(User user) {
        return UserRegistrationResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserPutResponse userToUserPutResponse(User user) {
        return UserPutResponse.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserTaskTimeClearResponse userToUserTaskTimeClearResponse(User user) {
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

    public UserTaskEffortResponse userToUserTaskEffortResponse(User user) {
        List<TaskTimeShortSpent> taskTimeShortSpents = new ArrayList<>();
        for(Task task: user.getTasks())
            for(TaskTime taskTime: task.getTaskTimes()) {
                taskTimeShortSpents.add(TaskTimeShortSpent.builder()
                        .taskId(taskTime.getTask().getId())
                        .timeSpent(Duration.between(taskTime.getStartTime(), taskTime.getStopTime()))
                    .build());
            }
        return UserTaskEffortResponse.builder()
            .userId(user.getId())
            .taskEfforts(taskTimeShortSpents)
            .build();
    }
}
