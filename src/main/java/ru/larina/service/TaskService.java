package ru.larina.service;

import jakarta.persistence.EntityManager;
import ru.larina.hibernate.EMFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.User;
import ru.larina.repository.TaskRepository;

public class TaskService implements TaskRepository {
    @Override
    public Task get(Long id) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            return em.find(Task.class, id);
        }
    }

    @Override
    public Task add(Task task) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();
            return task;
        }
    }

    @Override
    public Task update(Task task) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            Task taskToChange = this.get(task.getId());
            taskToChange.setName(task.getName());
            em.getTransaction().commit();
            return taskToChange;
        }
    }
}
