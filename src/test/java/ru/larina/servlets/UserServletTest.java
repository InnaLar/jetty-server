package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.larina.hibernate.EmFactory;
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
}
