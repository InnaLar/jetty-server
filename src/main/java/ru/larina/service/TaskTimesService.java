package ru.larina.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.dto.taskTime.TaskTimeRs;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.jpa.TaskRepository;
import ru.larina.repository.jpa.TaskTimeRepository;
import ru.larina.repository.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskTimesService {
    private final TaskTimeRepository taskTimeRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskTimeMapper taskTimeMapper;

    public TaskTimeRs start(final Long taskId) {
        final Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, taskId));

        final TaskTime taskTime = TaskTime.builder()
            .task(task)
            .startTime(LocalDateTime.now())
            .build();

        final TaskTime taskTimeAdded = taskTimeRepository.save(taskTime);
        return taskTimeMapper.taskTimeToTaskTimeResponse(taskTimeAdded);
    }

    public TaskTimeRs stop(final Long taskId) {
        final TaskTime taskTime = taskTimeRepository.findFirstByTaskIdOrderByIdDesc(taskId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_004, taskId));
        taskTime.setStopTime(LocalDateTime.now());
        final TaskTime taskTimeUpdated = taskTimeRepository.save(taskTime);
        return taskTimeMapper.taskTimeToTaskTimeResponse(taskTimeUpdated);
    }
}
