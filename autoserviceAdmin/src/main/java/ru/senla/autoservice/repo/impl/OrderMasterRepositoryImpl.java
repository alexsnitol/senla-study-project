package ru.senla.autoservice.repo.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.model.OrderMaster;
import ru.senla.autoservice.model.OrderStatusEnum;
import ru.senla.autoservice.repo.IOrderMasterRepository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class OrderMasterRepositoryImpl extends AbstractRepositoryImpl<OrderMaster> implements IOrderMasterRepository {


    @Override
    public Map<String, List<javax.persistence.criteria.Order>> getSortMap(Root<OrderMaster> root) {
        return Collections.emptyMap();
    }

    @Override
    public List<Predicate> getPredicateList(Root<OrderMaster> root, MultiValueMap<String, String> requestParams) {
        return Collections.emptyList();
    }

    private Predicate getPredicateOfOrderStatusList(Join<OrderMaster, Order> orderJoin,
                                                    List<OrderStatusEnum> orderStatusList) {

        return criteriaBuilder.or(
                orderStatusList.stream()
                        .map(status -> criteriaBuilder.equal(orderJoin.get("status"), status))
                        .toArray(Predicate[]::new)
        );
    }

    private Predicate getPredicateOfRangeOfTimeOfCompletion(Join<OrderMaster, Order> orderJoin,
                                                            LocalDateTime from, LocalDateTime to) {

        return criteriaBuilder.between(orderJoin.get("timeOfCompletion"), from, to);
    }

    @Override
    public List<Master> findAllMastersByTimeOfCompletionAndOrderStatusList(LocalDateTime from, LocalDateTime to,
                                                                           List<OrderStatusEnum> orderStatusList) {

        CriteriaQuery<Master> criteriaQuery = criteriaBuilder.createQuery(Master.class);
        Root<OrderMaster> orderMasterRoot = criteriaQuery.from(OrderMaster.class);
        Join<OrderMaster, Order> orderJoin = orderMasterRoot.join("order");
        Join<OrderMaster, Master> masterJoin = orderMasterRoot.join("master");

        criteriaQuery.select(masterJoin)
                .where(getPredicateOfOrderStatusList(orderJoin, orderStatusList),
                        getPredicateOfRangeOfTimeOfCompletion(orderJoin, from, to)
                )
                .groupBy(masterJoin.get("id"));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
