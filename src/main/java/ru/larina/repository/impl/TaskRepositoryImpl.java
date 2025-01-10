package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import org.hibernate.query.Query;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.entity.Task;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.model.projections.TotalWorkByPeriodProjection;
import ru.larina.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskRepositoryImpl extends SimpleCrudRepository<Task, Long> implements TaskRepository {
    public TaskRepositoryImpl() {
        super(Task.class);
    }

    @Override
    public List<TaskTimeShortSpentProjection> getUserTaskEffortsByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final List<TaskTimeShortSpentProjection> taskTimeShortSpentProjections =
                em.createQuery(
                        """
                                select new TaskTimeShortSpentProjection(tt.task.id as id, sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as sumTime)
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.startTime > :startTime
                                and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                                group by tt.task.id
                            """, TaskTimeShortSpentProjection.class)
                    .setParameter("startTime", startTime)
                    .setParameter("stopTime", stopTime)
                    .setParameter("userId", userId)
                    .getResultList();
            return taskTimeShortSpentProjections;
        }
    }

    @Override
    public List<TaskTimeLongSpentProjection> getUserWorkIntervalByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final List<TaskTimeLongSpentProjection> taskTimeLongSpentProjections = em.createQuery(
                    """
                            select new TaskTimeLongSpentProjection(tt.task.id as id,
                             to_char(tt.startTime, 'DD-MM-YYYY HH24:MI') as startDateTime,
                             to_char(tt.stopTime, 'DD-MM-YYYY HH24:MI') as stopDateTime,
                             coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime as timeSpent)
                            from TaskTime tt
                            join tt.task t
                            where t.user.id = :userId
                            and tt.startTime > :startTime
                            and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                            order by tt.startTime
                        """, TaskTimeLongSpentProjection.class)
                .setParameter("startTime", startTime)
                .setParameter("stopTime", stopTime)
                .setParameter("userId", userId)
                .getResultList();
            return taskTimeLongSpentProjections;
        }
    }

    @Override
    public TotalWorkByPeriodProjection getUserTotalWorkByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final TotalWorkByPeriodProjection totalTimeSpent =
                em.createQuery(
                        """
                                select new TotalWorkByPeriodProjection(sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as totalTime)
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.startTime > :startTime
                                and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                            """, TotalWorkByPeriodProjection.class)
                    .setParameter("startTime", startTime)
                    .setParameter("stopTime", stopTime)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return totalTimeSpent;
        }
    }

    @Override
    public void clearTaskTimes(final Long userId) {
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
    public List<TaskTimeId> getEmptyTaskTimeByUser(final Long userId) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final List<TaskTimeId> taskTimeIds =
                em.createQuery(
                        """
                                select new TaskTimeId(tt.id)
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.disabled = true
                            """, TaskTimeId.class)
                    .setParameter("userId", userId)
                    .getResultList();

            return taskTimeIds;
        }
    }
}
