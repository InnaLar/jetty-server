package ru.larina.repository;

import ru.larina.model.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    void clearTaskTimes(User user);
}
