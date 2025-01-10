package ru.larina.repository;

import java.util.Optional;

public interface CrudRepository<T, I> {

    T save(T entity);

    Optional<T> findById(I primaryKey);
}
