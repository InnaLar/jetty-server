package ru.larina.service;

import lombok.AllArgsConstructor;
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
        TaskTime taskTime = TaskTime.builder()
            .task(taskRepository.findById(taskId).get())
            .startTime(LocalDateTime.now())
            .build();
        TaskTime taskTimeAdded = taskTimeRepository.save(taskTime);
        return TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTimeAdded);
    }

    public TaskTimeResponse stop(Long taskId) {
        TaskTime taskTime = TaskTime.builder()
            .task(taskRepository.findById(taskId).get())
            .stopTime(LocalDateTime.now())
            .build();
        TaskTime taskTimeAdded = taskTimeRepository.save(taskTime);
        return TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTimeAdded);
    }

    public UserTaskTimeClearResponse clear(Long userId) {
        User user = userRepository.findById(userId).get();
        for (Task task : user.getTasks()) {
            for(TaskTime taskTime: task.getTaskTimes()){
                taskTimeRepository.clear(taskTime);
            }
        }
        return UserMapper.userToUserTaskTimeClearResponse(user);
    }
}
