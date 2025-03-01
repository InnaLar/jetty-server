package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.user.UserPutRq;
import ru.larina.model.dto.user.UserPutRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userClear.UserDeleteTasksRs;
import ru.larina.model.entity.User;

@SuppressWarnings("UnusedLocalVariable")
class UserServletTest extends IntegrationTestBase {

    @Test
    void addUserShouldSuccess() {
        //GIVEN
        final UserRegistrationRq request = UserRegistrationRq.builder()
            .email("test@mail.ru")
            .build();

        //WHEN
        final UserRegistrationRs response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getEml()).isEqualTo(request.getEmail());

        try (EntityManager entityManager = EmFactory.getEntityManager()) {
            entityManager.getTransaction().begin();
            final User user = entityManager.find(User.class, response.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getId())
                .isEqualTo(response.getId());

            entityManager.getTransaction().commit();
        }
    }

    @Test
    void updateUserShouldSuccess() throws InterruptedException {
        //GIVEN
        final UserRegistrationRq request = UserRegistrationRq.builder()
            .email("test12234@mail.ru")
            .build();

        final UserRegistrationRs response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        assert response != null;
        final UserPutRq requestForUpdate = UserPutRq.builder()
            .id(response.getId())
            .email("InnaLarrgj@mail.ru")
            .build();

        //WHEN
        final UserPutRs responseUpdated = webClient.put()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestForUpdate)
            .retrieve()
            .bodyToMono(UserPutRs.class)
            .block();

        //THEN
        Assertions.assertThat(responseUpdated).isNotNull();
        Assertions.assertThat(requestForUpdate.getId()).isEqualTo(responseUpdated.getId());
        Assertions.assertThat(requestForUpdate.getEmail()).isEqualTo(responseUpdated.getEml());

        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final User user = em.find(User.class, requestForUpdate.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getId()).isEqualTo(requestForUpdate.getId());
            Assertions.assertThat(user.getEmail()).isEqualTo(requestForUpdate.getEmail());
            em.getTransaction().commit();
        }

    }

    @Test
    void deleteUserTasksShouldSuccess() {
        //GIVEN
        final UserRegistrationRq request = UserRegistrationRq.builder()
            .email("test3@mail.ru")
            .build();

        final UserRegistrationRs response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationRs.class)
            .block();

        Assertions.assertThat(response).isNotNull();

        final TaskCreationRq requestTask = TaskCreationRq.builder()
            .userId(response.getId())
            .name("task1")
            .build();

        //WHEN
        final UserDeleteTasksRs responseDeleteUserTasks = webClient.post()
            .uri(uriBuilder -> uriBuilder.path("api/v1/user/clear")
                .queryParam("userId", response.getId())
                .build())
            .retrieve()
            .bodyToMono(UserDeleteTasksRs.class)
            .block();
        //THEN
        Assertions.assertThat(responseDeleteUserTasks).isNotNull();
        Assertions.assertThat(responseDeleteUserTasks.getUserId()).isEqualTo(response.getId());

        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            final User user = em.find(User.class, response.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getTasks()).isEmpty();
            em.getTransaction().commit();
        }

    }
}
