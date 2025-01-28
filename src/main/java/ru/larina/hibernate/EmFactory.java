package ru.larina.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EmFactory {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(
            "myPersistenceUnit"
        );

    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void initialize(String jdbsUrl, String user, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("javax.persistence.jdbc.url", jdbsUrl);
        map.put("javax.persistence.jdbc.user", user);
        map.put("javax.persistence.jdbc.password", password);

        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("myPersistenceUnit", map);
    }
}
