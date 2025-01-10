package ru.larina.servlets;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.web.reactive.function.client.WebClient;
import ru.larina.SimpleHttpServer;
import ru.larina.hibernate.EmFactory;
import ru.larina.service.MyPostgresSQLContainer;

@Slf4j
public class IntegrationTestBase extends MyPostgresSQLContainer {
    private static boolean needStartServer = true;
    protected final WebClient webClient = createWebClient();

    private static WebClient createWebClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .build();
    }

    @SneakyThrows
    @BeforeAll
    static void setUp() {
        final Runnable runnable = () -> {

            try {
                SimpleHttpServer.main(new String[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        };
        if (needStartServer) {
            new Thread(runnable).start();

            Thread.sleep(2000);
            needStartServer = false;
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager entityManager = EmFactory.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("TRUNCATE TABLE users, tasks, task_time CASCADE")
                .executeUpdate();
            entityManager.getTransaction().commit();
        }
        log.info("After truncate");
    }

}
