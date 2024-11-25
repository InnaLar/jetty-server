package ru.larina.repository;

import java.io.Serializable;
import java.util.Optional;

public interface CrudRepository<T, ID extends Serializable> {

    T save(T entity);

    Optional<T> findById(ID primaryKey);

    //Iterable<T> findAll();

    //Long count();

    //void delete(T entity);

    //boolean exists(ID primaryKey);
}
