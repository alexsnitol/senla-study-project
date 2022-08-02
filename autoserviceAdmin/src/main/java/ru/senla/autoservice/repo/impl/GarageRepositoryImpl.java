package ru.senla.autoservice.repo.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.repo.IGarageRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class GarageRepositoryImpl extends AbstractRepositoryImpl<Garage> implements IGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(Garage.class);
    }


    /**
     * @param requestParams Map of String parameter and String value:
     *                      for GarageRepository have not implemented parameters
     */
    @Override
    public List<Garage> findAll(MultiValueMap<String, String> requestParams) {
        return super.findAll(requestParams);
    }

    public Garage findByOrderId(Long orderId) {
        CriteriaQuery<Garage> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<OrderGarage> orderGarageRoot = criteriaQuery.from(OrderGarage.class);

        criteriaQuery
                .select(orderGarageRoot.get("garage"))
                .where(getPredicateOfOrderId(orderGarageRoot, orderId));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Map<String, List<javax.persistence.criteria.Order>> getSortMap(Root<Garage> root) {
        return Collections.emptyMap();
    }

    @Override
    public List<Predicate> getPredicateList(Root<Garage> root, MultiValueMap<String, String> requestParams) {
        return Collections.emptyList();
    }

    private Predicate getPredicateOfOrderId(Root<OrderGarage> root, Long orderId) {
        Join<OrderGarage, Order> orderJoin = root.join("order");
        return criteriaBuilder.equal(orderJoin.get("id"), orderId);
    }
}