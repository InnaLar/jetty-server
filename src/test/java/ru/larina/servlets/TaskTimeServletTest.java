package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.model.dto.taskTime.TaskTimeId;
import ru.larina.model.dto.taskTime.TaskTimeRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;

import java.util.List;

public class TaskTimeServletTest extends IntegrationTestBase {
    @Test
    void startTaskTimeByTaskShouldAccess() {
        //GIVEN
        final UserRegistrationRq requestUser = UserRegistrationRq.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationRs responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        assert responseUser != null;
        final TaskCreationRq requestTask = TaskCreationRq.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationRs responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationRs.class)
            .block();

        //WHEN
        final TaskTimeRs response = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTaskId()).isEqualTo(responseTask.getId());
    }

    @Test
    void stopTaskTimeShouldSuccess() {
        final UserRegistrationRq requestUser = UserRegistrationRq.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationRs responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        assert responseUser != null;
        final TaskCreationRq requestTask = TaskCreationRq.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationRs responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationRs.class)
            .block();
        Assertions.assertThat(responseTask).isNotNull();
        final TaskTimeRs responseTaskTime = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();
        Assertions.assertThat(responseTaskTime).isNotNull();
        //WHEN
        final TaskTimeRs response = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTaskId()).isEqualTo(responseTaskTime.getTaskId());
        Assertions.assertThat(response.getTaskTimeId()).isEqualTo(responseTaskTime.getTaskTimeId());
        Assertions.assertThat(response.getStartDateTime()).isEqualTo(responseTaskTime.getStartDateTime());
        Assertions.assertThat(response.getStopDateTime()).isNotNull();
    }

    @Test
    void clearTaskTimesByUser() {
        //GIVEN
        final UserRegistrationRq requestUser = UserRegistrationRq.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationRs responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        assert responseUser != null;

        final TaskCreationRq requestTask = TaskCreationRq.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationRs responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationRs.class)
            .block();

        Assertions.assertThat(responseTask).isNotNull();

        final TaskTimeRs responseTaskTimeStart = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();
        Assertions.assertThat(responseTaskTimeStart).isNotNull();

        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();

        //WHEN
        final UserTaskTimeClearRs response = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("api/v1/task-time/clear").queryParam("userId", responseUser.getId()).build())
            .retrieve()
            .bodyToMono(UserTaskTimeClearRs.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTaskTimeIds().getFirst()).isNotNull();

        final List<TaskTimeId> taskTimeIdByUser;

        try (EntityManager em = EmFactory.getEntityManager()) {
            taskTimeIdByUser = em.createQuery(
                    """
                            select tt.id
                            from TaskTime tt
                            join tt.task t
                            where t.user.id = :userId
                            and tt.disabled = true
                        """, TaskTimeId.class)
                .setParameter("userId", responseUser.getId())
                .getResultList();
        }
        Assertions.assertThat(taskTimeIdByUser.size()).isEqualTo(1);
        Assertions.assertThat(taskTimeIdByUser.getFirst().getId()).isEqualTo(responseTaskTimeStart.getTaskTimeId());
    }
}
