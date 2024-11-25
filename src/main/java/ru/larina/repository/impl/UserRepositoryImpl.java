package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional<User> findById(Long id) {
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
                Optional<User> userToChange = findById(user.getId());
                if (userToChange.isPresent()) {
                    User userGot = userToChange.get();
                    userGot.setEmail(user.getEmail());
                    em.merge(userGot);
                    user = userGot;
                } else {
                    throw new ServiceException(ErrorCode.ERR_CODE_002, user.getId());
                }
            }
            em.getTransaction().commit();
            return user;
        }
    }

    /*@Override
    public User update(User user) {
        try (EntityManager em = EMFactory.getEntityManager()) {
            em.getTransaction().begin();
            User userToChange = this.get(user.getId());
            userToChange.setEmail(user.getEmail());
            em.getTransaction().commit();
            return userToChange;
        }
    }*/

    @Override
    public void clearTaskTimes(User user) {
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
