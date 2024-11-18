package ru.larina.service;

import jakarta.persistence.EntityManager;
import ru.larina.hibernate.EMFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

public class UserService implements UserRepository {
    @Override
    public User get(Long id) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            return em.find(User.class, id);
        }
    }

    @Override
    public User add(User user) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User update(User user) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            User userToChange = this.get(user.getId());
            userToChange.setEmail(user.getEmail());
            em.getTransaction().commit();
            return userToChange;
        }
    }

    @Override
    public void clearTaskTimes(User user) {
        try (EntityManager em = EMFactory.getEntityManager()) {
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
