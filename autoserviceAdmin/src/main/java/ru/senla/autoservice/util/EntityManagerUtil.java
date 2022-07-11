package ru.senla.autoservice.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EntityManagerUtil {
    private final EntityManagerFactory entityManagerFactory
            = Persistence.createEntityManagerFactory("ru.senla.autoservice");
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        if (entityManager != null) {
            if (entityManager.isOpen()) {
                return entityManager;
            }
        }

        entityManager = entityManagerFactory.createEntityManager();

        return entityManager;
    }
}
