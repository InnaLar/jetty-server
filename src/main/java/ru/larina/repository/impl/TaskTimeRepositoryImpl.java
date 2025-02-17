package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.Query;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.entity.TaskTime;
import ru.larina.repository.TaskTimeRepository;

import java.util.List;
import java.util.Optional;

public class TaskTimeRepositoryImpl extends SimpleCrudRepository<TaskTime, Long> implements TaskTimeRepository {

    public TaskTimeRepositoryImpl() {
        super(TaskTime.class);
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
    public void clearByUser(final Long userId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final Query query = (Query) em.createQuery(
                """
                    update TaskTime tt
                    set tt.disabled = true
                    where tt.task = any (from Task t where t.user.id = :userId)
                    """);
            query.setParameter("userId", userId);
            query.executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Override
    public List<TaskTimeId> getTaskTimesByUser(final Long userId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final TypedQuery<TaskTimeId> q = em.createQuery(
                """
                    SELECT new TaskTimeId(tt.id)
                    FROM User u
                    JOIN u.tasks t
                    JOIN t.taskTimes tt
                    WHERE user.id =:userId
                          AND disabled = true""",
                TaskTimeId.class)
                .setParameter("userId", userId);
            final List<TaskTimeId> taskTimeIds = q.getResultList();
            em.getTransaction().commit();
            return taskTimeIds;
        }
    }
}
