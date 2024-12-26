package ru.larina.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.TaskTimeRepository;
import ru.larina.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TaskTimesService {
    private final TaskTimeRepository taskTimeRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskTimeMapper taskTimeMapper;

    public TaskTimeResponse start(final Long taskId) {
        final Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, taskId));

        final TaskTime taskTime = TaskTime.builder()
            .task(task)
            .startTime(LocalDateTime.now())
            .build();

        final TaskTime taskTimeAdded = taskTimeRepository.save(taskTime);
        return taskTimeMapper.taskTimeToTaskTimeResponse(taskTimeAdded);
    }

    public TaskTimeResponse stop(final Long taskId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final TaskTime taskTime = taskTimeRepository.findFirstByTaskIdOrderByIdDesc(taskId)
                .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_004, taskId));
            taskTime.setStopTime(LocalDateTime.now());
            em.merge(taskTime);
            em.getTransaction().commit();
            return taskTimeMapper.taskTimeToTaskTimeResponse(taskTime);
        }
    }

    public UserTaskTimeClearResponse clearByUser(final Long userId) {
        taskTimeRepository.clearByUser(userId);
        try (EntityManager em = EmFactory.getEntityManager()) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, userId));
            List<TaskTimeId> taskTimeIds = taskTimeRepository.getTaskTimesByUser(userId);
            return userMapper.userToUserTaskTimeClearResponse(user, taskTimeIds);
        }
    }
}
