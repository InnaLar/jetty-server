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
            Optional<Task> task = taskRepository.findById(taskId);
            Long taskTimeId = (Long) em.createQuery(
                    "select max(id) " +
                        "from TaskTime tt " +
                        "where tt.task = :task")
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
            if (taskTime.getId() == null && taskTime.getStopTime() == null) {
                em.persist(taskTime);
            } else {
                TaskTime taskTimeLast = findFirstByTaskIdOrderByIdDesc(taskTime.getTask().getId());
                taskTimeLast.setStopTime(taskTime.getStopTime());
                em.merge(taskTimeLast);
                taskTime = taskTimeLast;
            }
            em.getTransaction().commit();
            return taskTime;
        }

    }
}
