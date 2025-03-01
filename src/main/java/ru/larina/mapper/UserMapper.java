package ru.larina.mapper;

import org.springframework.stereotype.Component;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.dto.user.UserPutRq;
import ru.larina.model.dto.user.UserPutRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userClear.UserDeleteTasksRs;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;
import ru.larina.model.dto.userReport.UserTaskEffortRs;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User userRegistrationRequestToUser(final UserRegistrationRq rq) {
        return User.builder()
            .email(rq.getEmail())
            .build();
    }

    public User userPutRequestToUser(final UserPutRq rq) {
        return User.builder()
            .id(rq.getId())
            .email(rq.getEmail())
            .build();
    }

    public UserRegistrationRs userToUserRegistrationResponse(final User user) {
        return UserRegistrationRs.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserPutRs userToUserPutResponse(final User user) {
        return UserPutRs.builder()
            .id(user.getId())
            .eml(user.getEmail())
            .build();
    }

    public UserTaskTimeClearRs userToUserTaskTimeClearResponse(final User user, final List<Long> taskTimeIds) {
        return UserTaskTimeClearRs.builder()
            .userId(user.getId())
            .taskTimeIds(taskTimeIds)
            .build();
    }

    public UserTaskEffortRs userToUserTaskEffortResponse(final User user) {
        final List<TaskTimeShortSpent> taskTimeShortSpents = new ArrayList<>();
        for (Task task : user.getTasks()) {
            for (TaskTime taskTime : task.getTaskTimes()) {
                taskTimeShortSpents.add(TaskTimeShortSpent.builder()
                    .taskId(taskTime.getTask().getId())
                    .timeSpent(Duration.between(taskTime.getStartTime(), taskTime.getStopTime()))
                    .build());
            }
        }
        return UserTaskEffortRs.builder()
            .userId(user.getId())
            .taskEfforts(taskTimeShortSpents)
            .build();
    }

    public UserDeleteTasksRs userToUserDeleteTasksResponse(final User user) {
        return UserDeleteTasksRs.builder()
            .userId(user.getId())
            .build();
    }
}
