package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.mapper.TaskMapper;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskCreationResponse save(TaskCreationRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, request.getUserId()));
        Task task = taskMapper.taskCreationRequestToTask(request, user);
        Task taskAdded = taskRepository.save(task);
        return taskMapper.taskToTaskCreationResponse(taskAdded);
    }

    public UserTaskEffortResponse getUserTaskEffortResponse(Long userId, LocalDateTime startTime, LocalDateTime stopTime) {
        List<TaskTimeShortSpent> taskTimeShortSpents = taskRepository.getUserTaskEfforts(userId, startTime, stopTime);
        return UserTaskEffortResponse.builder()
            .userId(userId)
            .taskEfforts(taskTimeShortSpents)
            .build();
    }
}
