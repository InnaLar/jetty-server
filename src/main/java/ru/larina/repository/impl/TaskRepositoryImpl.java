package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
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
    public Optional<Task> findById(Long id) {
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

    @Override
    public List<TaskTimeShortSpent> getUserTaskEfforts(Long userId, LocalDateTime startTime, LocalDateTime stopTime) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            List<Tuple> taskDTOs =
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
            List<TaskTimeShortSpent> taskTimeShortSpents = new ArrayList<>();
            for (Tuple taskDTO : taskDTOs) {
                BigDecimal secsDecimal = (BigDecimal) taskDTO.get("timeSpent");
                Long nanoSecs = (secsDecimal).longValue();
                Duration duration = SecondToDuration.getDurationfromSeconds(nanoSecs);
                taskTimeShortSpents.add(TaskTimeShortSpent.builder()
                    .taskId((Long) taskDTO.get("id"))
                    .timeSpent(duration)
                    .build());
            }
            taskTimeShortSpents.sort((ts1, ts2) -> Math.toIntExact(ts2.getTimeSpent().toSeconds() - ts1.getTimeSpent().toSeconds()));
            return taskTimeShortSpents;
        }
    }
}
