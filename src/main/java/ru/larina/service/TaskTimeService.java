package ru.larina.service;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
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
public class TaskTimeService {
    TaskTimeRepository taskTimeRepository;
    TaskRepository taskRepository;
    UserRepository userRepository;

    public TaskTimeResponse start(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        TaskTime taskTime = TaskTime.builder()
            .task(task)
            .startTime(LocalDateTime.now())
            .build();
        taskTimeRepository.save(taskTime);
        return TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTime);
    }

    public TaskTimeResponse stop(Long taskId) {
        TaskTime taskTime;
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            taskTime = taskTimeRepository.findFirstByTaskIdOrderByIdDesc(taskId);
            taskTime.setStopTime(LocalDateTime.now());
            em.getTransaction().commit();;
        }
        return TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTime);
    }

    public UserTaskTimeClearResponse clear(Long userId) {
        User user = userRepository.findById(userId).get();
        for (Task task : user.getTasks()) {
            for (TaskTime taskTime : task.getTaskTimes()) {
                taskTimeRepository.clear(taskTime);
            }
        }
        return UserMapper.userToUserTaskTimeClearResponse(user);
    }
}
