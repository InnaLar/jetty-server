package ru.larina.repository;

import ru.larina.model.entity.User;

public interface UserRepository {
    User get(Long id);
    User add(User user);
    User update(User user);
    void clearTaskTimes(User user);
}
