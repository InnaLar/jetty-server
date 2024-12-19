package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.web.reactive.function.client.WebClient;
import ru.larina.SimpleHttpServer;
import ru.larina.hibernate.EmFactory;

public class IntegrationTestBase {
    protected final WebClient webClient = createWebClient();

    private static WebClient createWebClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();
    }

    @SneakyThrows
    @BeforeAll
    static void setUp() {
        Runnable runnable = () -> {
            try {
                SimpleHttpServer.main(new String[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(runnable).start();

        Thread.sleep(2000);
    }

    @AfterEach
    void tearDown() {
        try (EntityManager entityManager = EmFactory.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("TRUNCATE TABLE users CASCADE")
                .executeUpdate();

            entityManager.getTransaction().commit();
        }
    }

}
