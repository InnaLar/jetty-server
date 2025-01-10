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
import ru.larina.model.dto.userReportDTO.UserTotalWorkByPeriodResponse;
import ru.larina.model.dto.userReportDTO.UserWorkIntervalsResponse;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class ReportServletTest extends IntegrationTestBase {
    @Test
    void getUserEffortShouldBeSuccess() {
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

        final TaskTimeResponse responseTaskTimeStop = webClient.post()
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
        final UserTaskEffortResponse response = webClient.get()
            .uri(uriBuilder -> uriBuilder.path("api/v1/report/getUserTaskEfforts")
                .queryParam("userId", responseUser.getId())
                .queryParam("startDateTime", responseTaskTimeStart.getStartDateTime()
                    .minusMinutes(1L)
                    .format(formatter))
                .queryParam("stopDateTime", responseTaskTimeStop.getStopDateTime()
                    .plusMinutes(1L)
                    .format(formatter))
                .build())
            .retrieve()
            .bodyToMono(UserTaskEffortResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUserId()).isEqualTo(responseUser.getId());
        Assertions.assertThat(response.getTaskEfforts().getFirst().getTimeSpent())
            .hasMinutes(Duration.between(responseTaskTimeStop.getStartDateTime(), responseTaskTimeStop.getStopDateTime()).toMinutesPart());
    }

    @Test
    void getUserWorkIntervalShouldBeSuccess() {
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

        final TaskTimeResponse responseTaskTimeStop = webClient.post()
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
        final UserWorkIntervalsResponse response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/report/getUserWorkIntervalsByPeriod")
                .queryParam("userId", responseUser.getId())
                .queryParam("startDateTime", responseTaskTimeStop.getStartDateTime()
                    .minusMinutes(1L)
                    .format(formatter))
                .queryParam("stopDateTime", responseTaskTimeStop.getStopDateTime()
                    .plusMinutes(1L)
                    .format(formatter))
                .build())
            .retrieve()
            .bodyToMono(UserWorkIntervalsResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUserId()).isEqualTo(responseUser.getId());
        Assertions.assertThat(response.getWorkIntervals().getFirst().getStartDateTime().getMinute())
            .isEqualTo(responseTaskTimeStart.getStartDateTime().getMinute());
        Assertions.assertThat(response.getWorkIntervals().getFirst().getStopDateTime().getMinute())
            .isEqualTo(responseTaskTimeStop.getStopDateTime().getMinute());
        Assertions.assertThat(response.getWorkIntervals().size())
            .isEqualTo(1);
        Assertions.assertThat(response.getWorkIntervals().getFirst().getTimeSpent().toMinutesPart())
            .isEqualTo(Duration.between(responseTaskTimeStart.getStartDateTime(), responseTaskTimeStop.getStopDateTime()).toMinutesPart());
    }

    @Test
    void getUserTotalWorkShouldBeSuccess() {
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

        final TaskTimeResponse responseTaskTimeStop = webClient.post()
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
        final UserTotalWorkByPeriodResponse response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/report/getUserTotalWorkByPeriod")
                .queryParam("userId", responseUser.getId())
                .queryParam("startDateTime", responseTaskTimeStop.getStartDateTime()
                    .minusMinutes(1L)
                    .format(formatter))
                .queryParam("stopDateTime", responseTaskTimeStop.getStopDateTime()
                    .plusMinutes(1L)
                    .format(formatter))
                .build())
            .retrieve()
            .bodyToMono(UserTotalWorkByPeriodResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUserId()).isEqualTo(responseUser.getId());
        Assertions.assertThat(response.getTotalWork())
            .hasMinutes(Duration.between(responseTaskTimeStop.getStartDateTime(), responseTaskTimeStop.getStopDateTime()).toMinutesPart());
    }
}
