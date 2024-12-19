package ru.larina.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.larina.mapper.UserMapper;
import ru.larina.repository.impl.UserRepositoryImpl;

class UserServiceTest {

    @Test
    @Disabled
    void getById() {

        //GIVEN
        final Long id = 1L;
        final UserService service = new UserService(new UserRepositoryImpl(), new UserMapper());

        Long totalDuration = 0L;
        for (int i = 0; i < 100; i++) {
            final Long start = System.currentTimeMillis();
            service.getById(id);
            final Long stop = System.currentTimeMillis();
            final long duration = stop - start;
            totalDuration += duration;
        }

        //THEN
        final long averageDuration = totalDuration / 100;
        System.out.printf("Average time: %d ms%n", averageDuration);
    }
}
