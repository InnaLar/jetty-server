package ru.larina.repository;

import org.springframework.data.jpa.repository.Query;
import ru.larina.model.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

    void deleteTasksByUser(Long userId);
}
