package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeId;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;

import java.util.List;

public class TaskTimeServletTest extends IntegrationTestBase {
    @Test
    void startTaskTimeByTaskShouldAccess() {
        //GIVEN
        final UserRegistrationRequest requestUser = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationResponse responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        assert responseUser != null;
        final TaskCreationRequest requestTask = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationResponse responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();

        //WHEN
        final TaskTimeResponse response = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getTaskId()).isEqualTo(responseTask.getId());
    }

    @Test
    void stopTaskTimeShouldSuccess() {
        final UserRegistrationRequest requestUser = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationResponse responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        assert responseUser != null;
        final TaskCreationRequest requestTask = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationResponse responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();
        Assertions.assertThat(responseTask).isNotNull();
        final TaskTimeResponse responseTaskTime = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();
        Assertions.assertThat(responseTaskTime).isNotNull();
        //WHEN
        final TaskTimeResponse response = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
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
        final UserRegistrationRequest requestUser = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        final UserRegistrationResponse responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        assert responseUser != null;

        final TaskCreationRequest requestTask = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        final TaskCreationResponse responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();

        Assertions.assertThat(responseTask).isNotNull();

        final TaskTimeResponse responseTaskTimeStart = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();
        Assertions.assertThat(responseTaskTimeStart).isNotNull();

        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();

        //WHEN
        final UserTaskTimeClearResponse response = webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("api/v1/task-time/clear").queryParam("userId", responseUser.getId()).build())
            .retrieve()
            .bodyToMono(UserTaskTimeClearResponse.class)
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
