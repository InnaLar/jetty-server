package ru.larina.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.mapper.TaskMapper;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.repository.jpa.TaskRepository;
import ru.larina.repository.jpa.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskCreationRs save(final TaskCreationRq request) {
        final User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, request.getUserId()));
        final Task task = taskMapper.taskCreationRequestToTask(request, user);
        final Task taskAdded = taskRepository.save(task);
        return taskMapper.taskToTaskCreationResponse(taskAdded);
    }
}
