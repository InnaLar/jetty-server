package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.hibernate.query.Query;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.entity.Task;
import ru.larina.repository.TaskRepository;
import ru.larina.service.SecondToDuration;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public Optional<Task> findById(final Long id) {
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
                final Optional<Task> taskToChange = findById(task.getId());
                if (taskToChange.isPresent()) {
                    final Task taskGot = taskToChange.get();
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

    @Override
    public List<TaskTimeShortSpent> getUserTaskEffortsByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final List<Tuple> taskDtos =
                em.createQuery(
                        """
                                select tt.task.id as id, sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as timeSpent
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.startTime > :startTime
                                and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                                group by tt.task.id
                            """, Tuple.class)
                    .setParameter("startTime", startTime)
                    .setParameter("stopTime", stopTime)
                    .setParameter("userId", userId)
                    .getResultList();
            final List<TaskTimeShortSpent> taskTimeShortSpents = new ArrayList<>();
            for (Tuple taskDto : taskDtos) {
                final BigDecimal secsDecimal = (BigDecimal) taskDto.get("timeSpent");
                final Long nanoSecs = secsDecimal.longValue();
                final Duration duration = SecondToDuration.getDurationfromSeconds(nanoSecs);
                taskTimeShortSpents.add(TaskTimeShortSpent.builder()
                    .taskId((Long) taskDto.get("id"))
                    .timeSpent(duration)
                    .build());
            }
            taskTimeShortSpents.sort((ts1, ts2) -> Math.toIntExact(ts2.getTimeSpent().toSeconds() - ts1.getTimeSpent().toSeconds()));
            return taskTimeShortSpents;
        }
    }

    @Override
    public List<TaskTimeLongSpent> getUserWorkIntervalByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final List<TaskTimeLongSpent> taskTimeLongSpents =
                em.createQuery(
                        """
                                select new TaskTimeLongSpent(tt.task.id as taskId,
                                 tt.startTime as startDateTime, tt.stopTime as stopDateTime,
                                  coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime as timeSpent)
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.startTime > :startTime
                                and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                                order by tt.startTime
                            """, TaskTimeLongSpent.class)
                    .setParameter("startTime", startTime)
                    .setParameter("stopTime", stopTime)
                    .setParameter("userId", userId)
                    .getResultList();

            return taskTimeLongSpents;
        }
    }

    @Override
    public Duration getUserTotalWorkByPeriods(final Long userId, final LocalDateTime startTime, final LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            final Tuple timeSpent =
                em.createQuery(
                        """
                                select sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as timeSpent
                                from TaskTime tt
                                join tt.task t
                                where t.user.id = :userId
                                and tt.startTime > :startTime
                                and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                            """, Tuple.class)
                    .setParameter("startTime", startTime)
                    .setParameter("stopTime", stopTime)
                    .setParameter("userId", userId)
                    .getSingleResult();

            Long nanoSecs = 0L;
            if (timeSpent.get("timeSpent") != null) {
                final BigDecimal timeSpentNanoSecs = (BigDecimal) timeSpent.get("timeSpent");
                nanoSecs = timeSpentNanoSecs.longValue();
            }

            return SecondToDuration.getDurationfromSeconds(nanoSecs);
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
