package ru.larina.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EmFactory {
    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory(
            "myPersistenceUnit"
        );

    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void initialize(final String jdbsUrl, final String user, final String password) {
        final Map<String, String> map = new HashMap<>();
        map.put("javax.persistence.jdbc.url", jdbsUrl);
        map.put("javax.persistence.jdbc.user", user);
        map.put("javax.persistence.jdbc.password", password);

        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit", map);
    }
}
