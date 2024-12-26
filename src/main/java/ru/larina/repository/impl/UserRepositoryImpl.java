package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

public class UserRepositoryImpl extends SimpleCrudRepository<User, Long> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public void deleteTasksByUser(Long userId) {
        /*try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final Query query = (Query) em.createQuery(
                """
                    update User u
                    set u.tasks = null
                    where u.id = :userId)
                    """);
            query.setParameter("userId", userId);
            query.executeUpdate();
            em.getTransaction().commit();
        }*/
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            User user = em.createQuery("""
                    select u
                    from User u
                    join fetch u.tasks t
                    where u.id = :userId
                    """, User.class)
                .setParameter("userId", userId)
                .getSingleResult();
            int countTasks = user.getTasks().size();
            for (int i = 0; i < countTasks; i++) {
                user.remove(user.getTasks().getFirst());
            }
            em.getTransaction().commit();
        }
    }
}
