package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.entity.Task;

public class TaskServletTest extends IntegrationTestBase {

    @Test
    void addTaskShouldSuccess() {
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
        final TaskCreationRq request = TaskCreationRq.builder()
            .userId(responseUser.getId())
            .name("task1")
            .build();

        //WHEN
        final TaskCreationRs response = webClient.post()
            .uri("api/v1/task")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(TaskCreationRs.class)
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
