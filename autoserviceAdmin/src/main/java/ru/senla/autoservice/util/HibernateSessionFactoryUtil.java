package ru.senla.autoservice.util;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderGarage;

@Slf4j
public class HibernateSessionFactoryUtil {
    private static final SessionFactory sessionFactory;

    private static ServiceRegistry serviceRegistry;

    static {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .addAnnotatedClass(Master.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(Garage.class)
                    .addAnnotatedClass(OrderGarage.class)
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                    .build();

            sessionFactory = metadata.getSessionFactoryBuilder()
                    //.applyBeanManager(getBeanManager())
                    .build();
        } catch (Throwable th) {
            log.error("Enitial SessionFactory creation failed {}", th.toString());
            throw new ExceptionInInitializerError(th);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static EntityManager createEntityManager() {
        return sessionFactory.createEntityManager();
    }
}
