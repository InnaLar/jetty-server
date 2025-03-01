package ru.larina.servlets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.model.dto.taskTime.TaskTimeRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userReport.UserTaskEffortRs;
import ru.larina.model.dto.userReport.UserTotalWorkByPeriodRs;
import ru.larina.model.dto.userReport.UserWorkIntervalsRs;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class ReportServletTest extends IntegrationTestBase {
    @Test
    void getUserEffortShouldBeSuccess() {
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

        final TaskTimeRs responseTaskTimeStop = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();
        Assertions.assertThat(responseTaskTimeStop).isNotNull();

        //WHEN
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final UserTaskEffortRs response = webClient.get()
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
            .bodyToMono(UserTaskEffortRs.class)
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

        final TaskTimeRs responseTaskTimeStop = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();
        Assertions.assertThat(responseTaskTimeStop).isNotNull();

        //WHEN
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final UserWorkIntervalsRs response = webClient.get()
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
            .bodyToMono(UserWorkIntervalsRs.class)
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

        final TaskTimeRs responseTaskTimeStop = webClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("api/v1/task-time/stop")
                .queryParam("taskId", responseTask.getId())
                .build())
            .retrieve()
            .bodyToMono(TaskTimeRs.class)
            .block();
        Assertions.assertThat(responseTaskTimeStop).isNotNull();

        //WHEN
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final UserTotalWorkByPeriodRs response = webClient.get()
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
            .bodyToMono(UserTotalWorkByPeriodRs.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUserId()).isEqualTo(responseUser.getId());
        Assertions.assertThat(response.getTotalWork())
            .hasMinutes(Duration.between(responseTaskTimeStop.getStartDateTime(), responseTaskTimeStop.getStopDateTime()).toMinutesPart());
    }
}
