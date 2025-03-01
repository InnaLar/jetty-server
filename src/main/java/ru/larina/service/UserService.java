package ru.larina.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.mapper.ReportMapper;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.taskTime.TaskTimeShortSpent;
import ru.larina.model.dto.user.UserPutRq;
import ru.larina.model.dto.user.UserPutRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userClear.UserDeleteTasksRs;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;
import ru.larina.model.dto.userReport.UserTaskEffortRs;
import ru.larina.model.entity.User;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.repository.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReportMapper reportMapper;

    public UserRegistrationRs getById(final Long id) {
        final User user = userRepository.findById(id)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, id));
        return userMapper.userToUserRegistrationResponse(user);
    }

    public UserRegistrationRs create(final UserRegistrationRq request) {
        final User user = userMapper.userRegistrationRequestToUser(request);
        final User userSaved = userRepository.save(user);
        return userMapper.userToUserRegistrationResponse(userSaved);
    }

    public UserPutRs update(final UserPutRq request) {
        final User user = userMapper.userPutRequestToUser(request);
        final User userUpdated = userRepository.save(user);
        return userMapper.userToUserPutResponse(userUpdated);
    }

    public UserDeleteTasksRs deleteTasksByUser(final Long id) {
        userRepository.deleteTasksByUser(id);
        final User user = User.builder()
            .id(id)
            .build();
        return userMapper.userToUserDeleteTasksResponse(user);
    }

    public UserTaskTimeClearRs clearByUser(final Long userId) {
        userRepository.clearByUser(userId);
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, userId));
        final List<Long> taskTimeIds = userRepository.getTaskTimesByUser(userId);
        return userMapper.userToUserTaskTimeClearResponse(user, taskTimeIds);
    }

    public UserTaskEffortRs getUserTaskEffortsByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        final List<TaskTimeShortSpentProjection> usertaskEfforts = userRepository.getUserTaskEffortsByPeriods(userId, startTime, stopTime);
//todo
        final List<TaskTimeShortSpent> taskTimeShortSpents = usertaskEfforts.stream()
            .map(reportMapper::taskTimeShortSpentProjectionToTaskTimeShortSpent)
            .toList();

        return UserTaskEffortRs.builder()
            .userId(userId)
            .taskEfforts(taskTimeShortSpents)
            .build();
    }

}
