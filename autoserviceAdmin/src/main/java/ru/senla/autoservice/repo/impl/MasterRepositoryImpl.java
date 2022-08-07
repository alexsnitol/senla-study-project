package ru.senla.autoservice.repo.impl;

import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.repo.IMasterRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MasterRepositoryImpl extends AbstractRepositoryImpl<Master> implements IMasterRepository {

    @PostConstruct
    public void init() {
        setClazz(Master.class);
    }


    /**
     * @param requestParams Map of String parameter and String value:
     *                      key - sortType:
     *                          AlphabeticallyAsc, AlphabeticallyDesc,
     *                          NumberOfActiveOrdersAsc, NumberOfActiveOrdersDesc;
     *                      key - orderId;
     */
    @Override
    public List<Master> findAll(MultiValueMap<String, String> requestParams) {
        return super.findAll(requestParams);
    }

    public Map<String, List<javax.persistence.criteria.Order>> getSortMap(Root<Master> root) {
        return Map.of(
                "AlphabeticallyAsc",
                List.of(
                        criteriaBuilder.desc(root.get("lastName")),
                        criteriaBuilder.desc(root.get("firstName")),
                        criteriaBuilder.desc(root.get("patronymic"))
                ),
                "AlphabeticallyDesc",
                List.of(
                        criteriaBuilder.asc(root.get("lastName")),
                        criteriaBuilder.asc(root.get("firstName")),
                        criteriaBuilder.asc(root.get("patronymic"))
                ),
                "NumberOfActiveOrdersAsc",
                List.of(
                        criteriaBuilder.asc(root.get("numberOfActiveOrders"))
                ),
                "NumberOfActiveOrdersDesc",
                List.of(
                        criteriaBuilder.desc(root.get("numberOfActiveOrders"))
                )
        );
    }

    @Override
    public List<Predicate> getPredicateList(Root<Master> root, MultiValueMap<String, String> requestParams) {
        List<Predicate> predicates = new ArrayList<>();

        if (requestParams.containsKey("orderId")) {
            Long orderId = Long.parseLong(requestParams.get("orderId").get(0));

            predicates.add(getPredicateOfOrderId(root, orderId));
        }

        return predicates;
    }

    private Predicate getPredicateOfOrderId(Root<Master> root, Long orderId) {
        Join<Master, Order> order = root.join("orders");
        return criteriaBuilder.equal(order.get("id"), orderId);
    }

    @Override
    public List<Master> findMastersByOrderId(Long orderId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("orderId", orderId.toString());
        return findAll(params);
    }

}