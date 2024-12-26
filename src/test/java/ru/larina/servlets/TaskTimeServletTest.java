package ru.larina.servlets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;

public class TaskTimeServletTest extends IntegrationTestBase {
    @Test
    void startTaskTimeByTaskShouldAccess() {
        //GIVEN
        UserRegistrationRequest requestUser = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        UserRegistrationResponse responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        assert responseUser != null;
        TaskCreationRequest requestTask = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        TaskCreationResponse responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();

        //WHEN
        TaskTimeResponse response = webClient.post()
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
        UserRegistrationRequest requestUser = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        UserRegistrationResponse responseUser = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestUser)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        assert responseUser != null;
        TaskCreationRequest requestTask = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        TaskCreationResponse responseTask = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestTask)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();

        TaskTimeResponse responseTaskTime = webClient.post()
            .uri(uriBuilder -> {
                assert responseTask != null;
                return uriBuilder
                    .path("api/v1/task-time/start")
                    .queryParam("taskId", responseTask.getId())
                    .build();
            })
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();
        //WHEN
        TaskTimeResponse response = webClient.post()
            .uri(uriBuilder -> {
                assert responseTask != null;
                return uriBuilder
                    .path("api/v1/task-time/stop")
                    .queryParam("taskId", responseTask.getId())
                    .build();
            })
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        assert responseTaskTime != null;
        Assertions.assertThat(response.getTaskId()).isEqualTo(responseTaskTime.getTaskId());
        Assertions.assertThat(response.getTaskTimeId()).isEqualTo(responseTaskTime.getTaskTimeId());
        Assertions.assertThat(response.getStartDateTime()).isEqualTo(responseTaskTime.getStartDateTime());
        Assertions.assertThat(response.getStopDateTime()).isNotNull();
    }
}
