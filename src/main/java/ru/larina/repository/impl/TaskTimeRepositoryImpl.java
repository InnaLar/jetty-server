package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.TaskTimeRepository;

import java.util.Optional;

@AllArgsConstructor
public class TaskTimeRepositoryImpl implements TaskTimeRepository {
    private TaskRepository taskRepository;

    @Override
    public Optional<TaskTime> findById(Long id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(TaskTime.class, id));
        }
    }

    @Override
    public TaskTime findFirstByTaskIdOrderByIdDesc(Long taskId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            //HQL todo only TaskTime
            Long taskTimeId = (Long) em.createQuery(
                    "select  " +
                        "from TaskTime tt " +
                        "where tt.task_id = :taskId")
                .setParameter("task", task.get())
                .getSingleResult();
            return this.findById(taskTimeId).get();
        }
    }

    @Override
    public void clear(TaskTime taskTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            taskTime.setDisabled(true);
            em.merge(taskTime);
        }
    }

    @Override
    public TaskTime save(TaskTime taskTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            if (taskTime.getId() == null) {
                em.persist(taskTime);
            } else {
                taskTime = em.merge(taskTime);
            }
            em.getTransaction().commit();
            return taskTime;
        }

    }
}
