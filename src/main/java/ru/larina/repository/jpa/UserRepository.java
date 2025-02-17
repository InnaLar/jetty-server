package ru.larina.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.larina.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
        delete
        from Task t
        where t.user_id = ?
        """, nativeQuery = true)
    void deleteTasksByUser(Long id);
}
