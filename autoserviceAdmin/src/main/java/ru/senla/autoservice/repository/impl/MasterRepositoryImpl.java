package ru.senla.autoservice.repository.impl;

import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.util.List;

@Singleton
public class MasterRepositoryImpl extends AbstractRepositoryImpl<Master> implements IMasterRepository {

    @PostConstruct
    public void init() {
        setClazz(Master.class);
    }

    @Override
    public List<Master> findMastersByOrderId(Long orderId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Master> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> root = criteriaQuery.from(Order.class);

        criteriaQuery
                .select(root.get("masters"))
                .where(criteriaBuilder.gt(root.get("id"), orderId));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void sortCriteriaQuery(CriteriaQuery<Master> cr, Root<Master> root, String sortType) {
        CriteriaBuilder criteriaBuilder = EntityManagerUtil.getEntityManager().getCriteriaBuilder();

        switch (sortType) {
            case "Alphabetically":
                cr.orderBy(
                        criteriaBuilder.asc(root.get("lastName")),
                        criteriaBuilder.asc(root.get("firstName")),
                        criteriaBuilder.asc(root.get("patronymic"))
                );
                break;
            case "AlphabeticallyDesc":
                cr.orderBy(
                        criteriaBuilder.desc(root.get("lastName")),
                        criteriaBuilder.desc(root.get("firstName")),
                        criteriaBuilder.desc(root.get("patronymic"))
                );
                break;
            case "NumberOfActiveOrders":
                cr.orderBy(
                        criteriaBuilder.asc(root.get("numberOfActiveOrders"))
                );
                break;
            case "NumberOfActiveOrdersDesc":
                cr.orderBy(
                        criteriaBuilder.desc(root.get("numberOfActiveOrders"))
                );
                break;
            default:
                break;
        }
    }

}