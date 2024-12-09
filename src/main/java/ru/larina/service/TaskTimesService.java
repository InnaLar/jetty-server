package ru.larina.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.TaskTimeRepository;
import ru.larina.repository.UserRepository;

import java.time.LocalDateTime;

@AllArgsConstructor
public class TaskTimesService {
    private final TaskTimeRepository taskTimeRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TaskTimeMapper taskTimeMapper;

    public TaskTimeResponse start(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, taskId));

        TaskTime taskTime = TaskTime.builder()
            .task(task)
            .startTime(LocalDateTime.now())
            .build();

        TaskTime taskTimeAdded = taskTimeRepository.save(taskTime);
        return taskTimeMapper.TaskTimeToTaskTimeResponse(taskTimeAdded);
    }

    public TaskTimeResponse stop(Long taskId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            TaskTime taskTime = taskTimeRepository.findFirstByTaskIdOrderByIdDesc(taskId)
                .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_004, taskId));
            taskTime.setStopTime(LocalDateTime.now());
            em.merge(taskTime);
            em.getTransaction().commit();
            return taskTimeMapper.TaskTimeToTaskTimeResponse(taskTime);
        }
    }

    public UserTaskTimeClearResponse clear(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, userId));
        for (Task task : user.getTasks()) {
            for (TaskTime taskTime : task.getTaskTimes()) {
                taskTimeRepository.clear(taskTime);
            }
        }
        return userMapper.userToUserTaskTimeClearResponse(user);
    }
}
