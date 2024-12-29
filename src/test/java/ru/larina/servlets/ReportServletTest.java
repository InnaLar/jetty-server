package ru.larina.servlets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class ReportServletTest extends IntegrationTestBase {
    @Test
    void getUserEffortShouldBeSuccess() {
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

        Assertions.assertThat(responseTask).isNotNull();

        TaskTimeResponse responseTaskTimeStart = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/start")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();
        Assertions.assertThat(responseTaskTimeStart).isNotNull();

        TaskTimeResponse responseTaskTimeStop = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeResponse.class)
            .block();
        Assertions.assertThat(responseTaskTimeStop).isNotNull();

        //WHEN
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        UserTaskEffortResponse response = webClient.get()
            .uri(uriBuilder -> uriBuilder.path("api/v1/report/getUserTaskEfforts")
                .queryParam("userId", responseUser.getId())
                .queryParam("startDateTime", responseTaskTimeStart.getStartDateTime().format(formatter))
                .queryParam("stopDateTime", responseTaskTimeStop.getStopDateTime().format(formatter))
                .build())
            .retrieve()
            .bodyToMono(UserTaskEffortResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUserId()).isEqualTo(responseUser.getId());
        Assertions.assertThat(response.getTaskEfforts().getFirst().getTimeSpent())
            .isEqualTo(Duration.between(responseTaskTimeStop.getStartDateTime(), responseTaskTimeStop.getStopDateTime()));
    }
}
