package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

@Slf4j
public class UserRepositoryImpl extends SimpleCrudRepository<User, Long> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public void deleteTasksByUser(final Long userId) {

        final EntityManager em = EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            final User user = em.createQuery("""
                    select u
                    from User u
                    join fetch u.tasks t
                    where u.id = :userId
                    """, User.class)
                .setParameter("userId", userId)
                .getSingleResult();
            final int countTasks = user.getTasks().size();
            for (int i = 0; i < countTasks; i++) {
                user.remove(user.getTasks().getFirst());
            }
            em.getTransaction().commit();
        } catch (NoResultException e) {
            log.info(e.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
