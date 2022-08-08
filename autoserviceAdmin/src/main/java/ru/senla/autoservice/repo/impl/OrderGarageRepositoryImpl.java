package ru.senla.autoservice.repo.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Garage;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderGarage;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IOrderGarageRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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
    public OrderGarage findOneFiltered(CriteriaQuery<OrderGarage> cr,
                                       Root<OrderGarage> root,
                                       List<Predicate> predicates) {

        root.fetch("order", JoinType.LEFT).fetch("masters", JoinType.LEFT);
        return super.findOneFiltered(cr, root, predicates);
    }

    @Override
    public <T> OrderGarage findOneFiltered(CriteriaQuery<OrderGarage> cr,
                                           Join<T, OrderGarage> join,
                                           List<Predicate> predicates) {

        join.fetch("order", JoinType.LEFT).fetch("masters", JoinType.LEFT);
        return super.findOneFiltered(cr, join, predicates);
    }

    @Override
    public List<OrderGarage> findAllFilteredAndSorted(CriteriaQuery<OrderGarage> cr,
                                                      Root<OrderGarage> root,
                                                      List<Predicate> predicates,
                                                      List<javax.persistence.criteria.Order> orders) {

        root.fetch("order", JoinType.LEFT).fetch("masters", JoinType.LEFT);
        return super.findAllFilteredAndSorted(cr, root, predicates, orders);
    }

    @Override
    public <T> List<OrderGarage> findAllFilteredAndSorted(CriteriaQuery<OrderGarage> cr,
                                                          Join<T, OrderGarage> join,
                                                          List<Predicate> predicates,
                                                          List<javax.persistence.criteria.Order> orders) {

        join.fetch("order", JoinType.LEFT).fetch("masters", JoinType.LEFT);
        return super.findAllFilteredAndSorted(cr, join, predicates, orders);
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
            Long orderId = Long.valueOf(requestParams.get("orderId").get(0));

            predicates.add(getPredicateOfOrderId(root, orderId));
        }

        if (requestParams.containsKey("garageId")) {
            Long garageId = Long.valueOf(requestParams.get("garageId").get(0));

            predicates.add(getPredicateOfGarageId(root, garageId));
        }

        if (requestParams.containsKey("status")) {
            List<OrderStatusEnum> statusList = requestParams.get("status").stream()
                    .map(OrderStatusEnum::valueOf)
                    .collect(Collectors.toList());

            predicates.add(getPredicateOfOrderStatusList(root, statusList));
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

    private Predicate getPredicateOfOrderStatusList(Root<OrderGarage> root, List<OrderStatusEnum> orderStatusList) {
        Join<OrderGarage, Order> orderJoin = root.join("order");
        return criteriaBuilder.or(
                orderStatusList.stream()
                        .map(status -> criteriaBuilder.equal(orderJoin.get("status"), status))
                        .toArray(Predicate[]::new)
        );
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

    @Override
    public List<OrderGarage> findAllByGarageIdAndOrderStatusList(Long garageId, List<OrderStatusEnum> orderStatuses) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap(
                Map.of(
                        "garageId", List.of(garageId.toString()),
                        "status", orderStatuses
                                .stream()
                                .map(OrderStatusEnum::name)
                                .collect(Collectors.toList())
                )
        );
        return findAll(params);
    }

}
