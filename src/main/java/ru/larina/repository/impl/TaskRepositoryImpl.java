package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.Task;
import ru.larina.repository.TaskRepository;

import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public Optional<Task> findById (Long id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(Task.class, id));
        }
    }

    @Override
    public Task save(Task task) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            if (task.getId() == null) {
                em.persist(task);
            } else {
                Optional<Task> taskToChange = findById(task.getId());
                if (taskToChange.isPresent()) {
                    Task taskGot = taskToChange.get();
                    taskGot.setName(task.getName());
                    em.merge(taskGot);
                    task = taskGot;
                } else {
                    throw new ServiceException(ErrorCode.ERR_CODE_002, task.getId());
                }
            }
            em.getTransaction().commit();
            return task;
        }
    }

}
