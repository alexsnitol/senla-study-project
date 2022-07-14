package ru.senla.autoservice.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityManagerUtil {
    private static final EntityManagerFactory entityManagerFactory
            = Persistence.createEntityManagerFactory("ru.senla.autoservice");
    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager != null) {
            if (entityManager.isOpen()) {
                return entityManager;
            }
        }

        entityManager = entityManagerFactory.createEntityManager();

        return entityManager;
    }
}
