package ru.senla.autoservice.repo.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IOrderRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements IOrderRepository {

    @PostConstruct
    public void init() {
        setClazz(Order.class);
    }

    @Override
    public Order findById(Long id) {
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<Order> orderRoot = criteriaQuery.from(clazz);
        orderRoot.fetch("masters", JoinType.LEFT);

        criteriaQuery
                .select(orderRoot)
                .where(criteriaBuilder.equal(orderRoot.get("id"), id));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * @param requestParams Map of String parameter and String value:
     *                      key - sortType:
     *                          TimeOfCreatedAsc, TimeOfCreatedDesc,
     *                          TimeOfBeginAsc, TimeOfBeginDesc,
     *                          TimeOfCompletionAsc, TimeOfCompletionDesc,
     *                          PriceAsc, PriceDesc;
     *                      key - status;
     *                      keys - fromTimeOfCompletion, toTimeOfCompletion;
     *                      key - minPrice, maxPrice;
     */
    @Override
    public List<Order> findAll(MultiValueMap<String, String> requestParams) {
        return super.findAll(requestParams);
    }

    @Override
    public List<Order> findAllFilteredAndSorted(CriteriaQuery<Order> cr, Root<Order> root, List<Predicate> predicates, List<javax.persistence.criteria.Order> orders) {
        root.fetch("masters", JoinType.LEFT);
        return super.findAllFilteredAndSorted(cr, root, predicates, orders);
    }

    @Override
    public <T> List<Order> findAllFilteredAndSorted(CriteriaQuery<Order> cr, Join<T, Order> join, List<Predicate> predicates, List<javax.persistence.criteria.Order> orders) {
        join.fetch("masters", JoinType.LEFT);
        return super.findAllFilteredAndSorted(cr, join, predicates, orders);
    }

    @Override
    public Map<String, List<javax.persistence.criteria.Order>> getSortMap(Root<Order> root) {
        return Map.of(
                "TimeOfCreatedAsc",
                List.of(
                        criteriaBuilder.asc(root.get("timeOfCreated"))
                ),
                "TimeOfCreatedDesc",
                List.of(
                        criteriaBuilder.desc(root.get("timeOfCreated"))
                ),
                "TimeOfBeginAsc",
                List.of(
                        criteriaBuilder.asc(root.get("timeOfBegin"))
                ),
                "TimeOfBeginDesc",
                List.of(
                        criteriaBuilder.desc(root.get("timeOfBegin"))
                ),
                "TimeOfCompletionAsc",
                List.of(
                        criteriaBuilder.asc(root.get("timeOfCompletion"))
                ),
                "TimeOfCompletionDesc",
                List.of(
                        criteriaBuilder.desc(root.get("timeOfCompletion"))
                ),
                "PriceAsc",
                List.of(
                        criteriaBuilder.asc(root.get("price"))
                ),
                "PriceDesc",
                List.of(
                        criteriaBuilder.desc(root.get("price"))
                )
        );
    }

    @Override
    public List<Predicate> getPredicateList(Root<Order> root, MultiValueMap<String, String> requestParams) {
        List<Predicate> predicates = new ArrayList<>();

        if (requestParams.containsKey("status")) {
            List<String> statusString = requestParams.get("status");
            List<OrderStatusEnum> statuses = statusString.stream()
                    .map(OrderStatusEnum::valueOf)
                    .collect(Collectors.toList());

            predicates.add(getPredicateOfStatusList(root, statuses));
        }

        if (requestParams.containsKey("fromTimeOfCompletion") && requestParams.containsKey("toTimeOfCompletion")) {
            LocalDateTime fromTimeOfCompletion = LocalDateTime.parse(requestParams.get("fromTimeOfCompletion").get(0));
            LocalDateTime toTimeOfCompletion = LocalDateTime.parse(requestParams.get("toTimeOfCompletion").get(0));

            predicates.add(getPredicateOfTimeOfCompletion(root, fromTimeOfCompletion, toTimeOfCompletion));
        }

        if (requestParams.containsKey("masterId")) {
            Long masterId = Long.valueOf(requestParams.get("masterId").get(0));

            predicates.add(getPredicateOfMasterId(root, masterId));
        }

        if (requestParams.containsKey("minPrice") && requestParams.containsKey("maxPrice")) {
            Float fromPrice = Float.valueOf(requestParams.get("fromPrice").get(0));
            Float toPrice = Float.valueOf(requestParams.get("toPrice").get(0));

            predicates.add(getPredicateOfPrice(root, fromPrice, toPrice));
        }

        return predicates;
    }

    private Predicate getPredicateOfPrice(Root<Order> root, Float from, Float to) {
        return criteriaBuilder.between(root.get("price"), from, to);
    }

    private Predicate getPredicateOfMasterId(Root<Order> root, Long masterId) {
        Join<Order, Master> master = root.join("masters");
        return criteriaBuilder.equal(master.get("id"), masterId);
    }

    private Predicate getPredicateOfTimeOfCompletion(Root<Order> root, LocalDateTime from, LocalDateTime to) {
        return criteriaBuilder.between(root.get("timeOfCompletion"), from, to);
    }

    private Predicate getPredicateOfStatus(Root<Order> root, OrderStatusEnum status) {
        return criteriaBuilder.equal(root.get("status"), status);
    }

    private Predicate getPredicateOfStatusList(Root<Order> root, List<OrderStatusEnum> statuses) {
        return root.get("status").in(statuses);
    }

    @Override
    public List<Order> findAllByStatus(OrderStatusEnum orderStatus) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", orderStatus.name());
        return findAll(params);
    }

    @Override
    public List<Order> findAllByStatuses(List<OrderStatusEnum> orderStatuses) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(
                Map.of(
                        "status",
                        orderStatuses.stream()
                                .map(OrderStatusEnum::name)
                                .collect(Collectors.toList())
                )
        );
        return findAll(params);
    }

    @Override
    public List<Order> findAllByTimeOfCompletion(LocalDateTime from, LocalDateTime to) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fromTimeOfCompletion", from.toString());
        params.add("toTimeOfCompletion", to.toString());
        return findAll(params);
    }

    @Override
    public List<Order> findAllByMasterId(Long masterId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("masterId", masterId.toString());
        return findAll(params);
    }

    @Override
    public List<Order> findAllByStatusAndMasterId(OrderStatusEnum orderStatus, Long masterId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", orderStatus.name());
        params.add("masterId", masterId.toString());
        return findAll(params);
    }

}