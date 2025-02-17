package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.task.TaskCreationRequest;
import ru.larina.model.dto.task.TaskCreationResponse;
import ru.larina.model.dto.user.UserRegistrationRequest;
import ru.larina.model.dto.user.UserRegistrationResponse;
import ru.larina.model.entity.Task;

public class TaskServletTest extends IntegrationTestBase {

    @Test
    void addTaskShouldSuccess() {
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
        final TaskCreationRequest request = TaskCreationRequest.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        //WHEN
        final TaskCreationResponse response = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(TaskCreationResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNameTask()).isEqualTo(request.getName());

        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final Task task = em.find(Task.class, response.getId());
            Assertions.assertThat(task).isNotNull();
            Assertions.assertThat(response.getId()).isEqualTo(task.getId());
            Assertions.assertThat(response.getNameTask()).isEqualTo(task.getName());
            em.getTransaction().commit();
        }
    }
}
