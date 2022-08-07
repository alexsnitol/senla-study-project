package ru.senla.autoservice.repo.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.repo.IOrderGarageRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class OrderGarageRepositoryImpl extends AbstractRepositoryImpl<OrderGarage> implements IOrderGarageRepository {

    @PostConstruct
    public void init() {
        setClazz(OrderGarage.class);
    }


    /**
     * @param requestParams Map of String parameter and String value:
     *                      key - orderId;
     *                      key - garageId;
     */
    @Override
    public List<OrderGarage> findAll(MultiValueMap<String, String> requestParams) {
        return super.findAll(requestParams);
    }

    @Override
    public Map<String, List<javax.persistence.criteria.Order>> getSortMap(Root<OrderGarage> root) {
        return Collections.emptyMap();
    }

    @Override
    public List<javax.persistence.criteria.Order> getOrderListOfSort(Root<OrderGarage> root, List<String> sortType) {
        return Collections.emptyList();
    }

    @Override
    public List<Predicate> getPredicateList(Root<OrderGarage> root, MultiValueMap<String, String> requestParams) {
        List<Predicate> predicates = new ArrayList<>();

        if (requestParams.containsKey("orderId")) {
            Long orderId = Long.parseLong(requestParams.get("orderId").get(0));

            predicates.add(getPredicateOfOrderId(root, orderId));
        }

        if (requestParams.containsKey("garageId")) {
            Long garageId = Long.parseLong(requestParams.get("garageId").get(0));

            predicates.add(getPredicateOfGarageId(root, garageId));
        }

        return predicates;
    }

    private Predicate getPredicateOfGarageId(Root<OrderGarage> root, Long garageId) {
        Join<OrderGarage, Garage> garage = root.join("garage");

        return criteriaBuilder.equal(garage.get("id"), garageId);
    }

    private Predicate getPredicateOfOrderId(Root<OrderGarage> root, Long orderId) {
        Join<OrderGarage, Order> order = root.join("order");

        return criteriaBuilder.equal(order.get("id"), orderId);
    }

    @Override
    public OrderGarage findByOrderId(Long orderId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("orderId", orderId.toString());
        return findAll(params).get(0);
    }

    @Override
    public List<OrderGarage> findByGarageId(Long garageId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("garageId", garageId.toString());
        return findAll(params);
    }

    @Override
    public OrderGarage findByOrderIdAndByGarageId(Long orderId, Long garageId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("orderId", orderId.toString());
        params.add("garageId", garageId.toString());
        return findAll(params).get(0);
    }
}
