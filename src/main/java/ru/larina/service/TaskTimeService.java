package ru.larina.service;

import jakarta.persistence.EntityManager;
import ru.larina.hibernate.EMFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.repository.TaskTimeRepository;

public class TaskTimeService implements TaskTimeRepository {
    TaskService taskService = new TaskService();

    @Override
    public TaskTime get(Long id) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            return em.find(TaskTime.class, id);
        }
    }

    @Override
    public TaskTime getLast(Long taskId) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            Task task = taskService.get(taskId);
            Long taskTimeId = (Long) em.createQuery(
                    "select max(id) " +
                        "from TaskTime tt " +
                        "where tt.task = :task")
                .setParameter("task", task)
                .getSingleResult();
            return this.get(taskTimeId);
        }
    }

    @Override
    public TaskTime add(TaskTime taskTime) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(taskTime);
            em.getTransaction().commit();
            return taskTime;
        }
    }

    @Override
    public TaskTime update(TaskTime taskTime) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            TaskTime taskToChange = this.get(taskTime.getId());
            taskToChange.setStartTime(taskTime.getStartTime());
            taskToChange.setStopTime(taskTime.getStopTime());
            em.merge(taskToChange);
            em.getTransaction().commit();
            return taskToChange;
        }
    }

    @Override
    public TaskTime clear(TaskTime taskTime) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            TaskTime taskToChange = this.get(taskTime.getId());
            taskToChange.setStartTime(null);
            taskToChange.setStopTime(null);
            em.merge(taskToChange);
            em.getTransaction().commit();
            return taskToChange;
        }
    }
}
