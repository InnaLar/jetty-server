package ru.larina.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMFactory {
    //private static EntityManagerFactory entityManagerFactory;
   /* public static EntityManager getEntityManager() {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
                "myPersistenceUnit"
            );
        return entityManagerFactory.createEntityManager();
    }*/
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
        "myPersistenceUnit"
    );
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
