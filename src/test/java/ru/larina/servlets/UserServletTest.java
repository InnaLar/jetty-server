package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
import ru.larina.model.dto.userClearDTO.UserDeleteTasksResponse;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.entity.User;

class UserServletTest extends IntegrationTestBase {

    @Test
    void addUserShouldSuccess() {
        //GIVEN
        UserRegistrationRequest request = UserRegistrationRequest.builder()
            .email("test@mail.ru")
            .build();

        //WHEN
        UserRegistrationResponse response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        //THEN
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getEml()).isEqualTo(request.getEmail());

        try (EntityManager entityManager = EmFactory.getEntityManager()) {
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, response.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getId())
                .isEqualTo(response.getId());

            entityManager.getTransaction().commit();
        }
    }

    @Test
    void updateUserShouldSuccess() throws InterruptedException {
        //GIVEN
        UserRegistrationRequest request = UserRegistrationRequest.builder()
            .email("test12234@mail.ru")
            .build();

        UserRegistrationResponse response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();


        assert response != null;
        UserPutRequest requestForUpdate = UserPutRequest.builder()
            .id(response.getId())
            .email("InnaLarrgj@mail.ru")
            .build();

        //WHEN
        UserPutResponse responseUpdated = webClient.put()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestForUpdate)
            .retrieve()
            .bodyToMono(UserPutResponse.class)
            .block();

        //THEN
        Assertions.assertThat(responseUpdated).isNotNull();
        Assertions.assertThat(requestForUpdate.getId()).isEqualTo(responseUpdated.getId());
        Assertions.assertThat(requestForUpdate.getEmail()).isEqualTo(responseUpdated.getEml());

        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, requestForUpdate.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getId()).isEqualTo(requestForUpdate.getId());
            Assertions.assertThat(user.getEmail()).isEqualTo(requestForUpdate.getEmail());
            em.getTransaction().commit();
        }

    }

    @Test
    void deleteUserTasksShouldSuccess() {
        //GIVEN
        UserRegistrationRequest request = UserRegistrationRequest.builder()
            .email("test2@mail.ru")
            .build();

        UserRegistrationResponse response = webClient.post()
            .uri("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(UserRegistrationResponse.class)
            .block();

        //WHEN
        UserDeleteTasksResponse responseDeleteUserTasks = webClient.post()
            .uri(uriBuilder -> {
                assert response != null;
                return uriBuilder.path("api/v1/user/clear")
                    .queryParam("userId", response.getId())
                    .build();
            })
            .retrieve()
            .bodyToMono(UserDeleteTasksResponse.class)
            .block();
        //THEN
        Assertions.assertThat(responseDeleteUserTasks).isNotNull();
        assert response != null;
        Assertions.assertThat(responseDeleteUserTasks.getUserId()).isEqualTo(response.getId());

        try (EntityManager em = EmFactory.getEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, response.getId());
            Assertions.assertThat(user).isNotNull();
            Assertions.assertThat(user.getTasks()).isEmpty();
            em.getTransaction().commit();
        }

    }
}
