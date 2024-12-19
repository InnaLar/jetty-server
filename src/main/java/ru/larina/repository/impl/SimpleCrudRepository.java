package ru.larina.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import ru.larina.hibernate.EmFactory;
import ru.larina.repository.CrudRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class SimpleCrudRepository<T, ID> implements CrudRepository<T, ID> {
    private final Class<T> entityClass;
    @Override

    public T save(T entity) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            T entityMerged = em.merge(entity);
            em.getTransaction().commit();
            return entityMerged;
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try (EntityManager em = EmFactory.getEntityManager()) {
            return Optional.ofNullable(em.find(entityClass, id));
        }
    }
}
