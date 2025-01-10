package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ru.larina.hibernate.EmFactory;
import ru.larina.repository.CrudRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class SimpleCrudRepository<T, I> implements CrudRepository<T, I> {
    private final Class<T> entityClass;

    @Override
    public T save(final T entity) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final T entityMerged = em.merge(entity);
            em.getTransaction().commit();
            return entityMerged;
        }
    }

    @Override
    public Optional<T> findById(final I id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(entityClass, id));
        }
    }
}
