package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.TaskTime;
import ru.larina.repository.TaskTimeRepository;

import java.util.Optional;

@AllArgsConstructor
public class TaskTimeRepositoryImpl implements TaskTimeRepository {

    @Override
    public Optional<TaskTime> findById(final Long id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(TaskTime.class, id));
        }
    }

    @Override
    public Optional<TaskTime> findFirstByTaskIdOrderByIdDesc(final Long taskId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.createQuery(
                    """
                        SELECT tt
                        FROM TaskTime tt
                        WHERE tt.task.id = :taskId
                        ORDER BY tt.startTime DESC
                        LIMIT 1""", TaskTime.class)
                .setParameter("taskId", taskId)
                .getSingleResult());
        }
    }

    @Override
    public void clear(final TaskTime taskTime) {
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
