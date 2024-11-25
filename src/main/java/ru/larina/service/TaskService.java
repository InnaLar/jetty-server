package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.mapper.TaskMapper;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.UserRepository;

import java.util.Optional;
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /*public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }*/

    public TaskCreationResponse save(TaskCreationRequest request) {
        Optional<User> user = userRepository.findById(request.getUserId());
        Task task = TaskMapper.taskCreationRequestToTask(request, user.get());
        Task taskAdded = taskRepository.save(task);
        return TaskMapper.taskToTaskCreationResponse(taskAdded);
    }
}
