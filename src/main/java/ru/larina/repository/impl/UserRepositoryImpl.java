package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findById(final Long id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(User.class, id));
        }
    }

    @Override
    public User save(User user) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            if (user.getId() == null) {
                em.persist(user);
            } else {
                user = em.merge(user);
            }
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public void clearTaskTimes(final User user) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            em.find(User.class, user.getId());
            for (Task task : user.getTasks()) {
                for (TaskTime taskTime : task.getTaskTimes()) {
                    task.removeTaskTime(taskTime);
                }
            }
            em.getTransaction().commit();
        }
    }
}
