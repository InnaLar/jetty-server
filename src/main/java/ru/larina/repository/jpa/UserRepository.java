package ru.larina.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.larina.model.entity.User;
import ru.larina.model.projections.TaskTimeLongSpentProjection;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.model.projections.TotalWorkByPeriodProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query(value = """
        delete
        from task_time t
        where t.task_id in (select id from tasks t
        where t.user_id = ?);

        delete
        from tasks t
        where t.user_id = ?
        """, nativeQuery = true)
    void deleteTasksByUser(Long userId, Long theSameUserId);

    @Transactional
    @Modifying
    @Query(value = """
           UPDATE task_time
           SET ds = true
           WHERE task_id = ANY (SELECT id FROM tasks WHERE user_id = ?)
        """, nativeQuery = true)
    void clearByUser(Long userId);

    @Query(value = """
           SELECT tt.id
           FROM users u
           JOIN tasks t ON t.user_id = u.id
           JOIN task_time tt ON tt.task_id = t.id
           WHERE u.id = ?
           AND tt.ds = true
        """, nativeQuery = true)
    List<Long> getTaskTimesByUser(Long userId);

    @Query(value = """
            select new ru.larina.model.projections.TaskTimeShortSpentProjection(tt.task.id as id, sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as sumTime)
            from TaskTime tt
            join tt.task t
            where t.user.id = :userId
                  and tt.startTime > :startTime
                  and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
                  group by tt.task.id
        """)
    List<TaskTimeShortSpentProjection> getUserTaskEffortsByPeriods(final @Param("userId") Long userId,
                                                                   final @Param("startTime") LocalDateTime startTime,
                                                                   final @Param("stopTime") LocalDateTime stopTime);

    @Query(value = """
        select new ru.larina.model.projections.TaskTimeLongSpentProjection(tt.task.id as id,
                             to_char(tt.startTime, 'DD-MM-YYYY HH24:MI') as startDateTime,
                             to_char(tt.stopTime, 'DD-MM-YYYY HH24:MI') as stopDateTime,
                             coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime as timeSpent)
        from TaskTime tt
        join tt.task t
        where t.user.id = :userId
              and tt.startTime > :startTime
              and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
        order by tt.startTime
        """)
    List<TaskTimeLongSpentProjection> getUserWorkIntervalByPeriods(final Long userId,
                                                                   final LocalDateTime startTime,
                                                                   final LocalDateTime stopTime);

    @Query(value = """
        select new ru.larina.model.projections.TotalWorkByPeriodProjection(sum(coalesce(tt.stopTime, CURRENT_DATE) - tt.startTime) as totalTime)
        from TaskTime tt
        join tt.task t
        where t.user.id = :userId
              and tt.startTime > :startTime
              and coalesce(tt.stopTime, CURRENT_DATE) < :stopTime
        """)
    TotalWorkByPeriodProjection getUserTotalWorkByPeriods(Long userId, LocalDateTime startTime, LocalDateTime stopTime);

}
