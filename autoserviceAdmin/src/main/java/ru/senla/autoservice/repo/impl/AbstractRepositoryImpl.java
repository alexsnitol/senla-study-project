package ru.senla.autoservice.repo.impl;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.AbstractModel;
import ru.senla.autoservice.repo.IAbstractRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractRepositoryImpl<M extends AbstractModel> implements IAbstractRepository<M> {

    @PersistenceContext
    protected EntityManager entityManager;
    protected CriteriaBuilder criteriaBuilder;
    protected Class<M> clazz;


    @PostConstruct
    public void initAbstract() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }


    public void setClazz(final Class<M> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet, null);
    }


    @Override
    public List<javax.persistence.criteria.Order> getOrderListOfSort(Root<M> root, List<String> sortType) {
        List<javax.persistence.criteria.Order> orderList = new ArrayList<>();

        Map<String, List<Order>> sortMap = getSortMap(root);

        sortType.forEach(sort -> {
            if (sortMap.containsKey(sort)) {
                orderList.addAll(sortMap.get(sort));
            }
        });

        return orderList;
    }

    @Override
    public M findById(Long id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public M findOne(MultiValueMap<String, String> requestParams) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        List<Predicate> predicates = getPredicateList(root, requestParams);

        return findOneFiltered(criteriaQuery, root, predicates);
    }

    @Override
    public List<M> findAll() {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        return findAllFilteredAndSorted(criteriaQuery, root, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public List<M> findAll(MultiValueMap<String, String> requestParams) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        List<Predicate> predicates = Collections.emptyList();
        List<Order> orders = Collections.emptyList();

        if (requestParams.containsKey("sortType") && requestParams.size() != 1
                || !requestParams.containsKey("sortType") && requestParams.size() != 0) {
            predicates = getPredicateList(root, requestParams);
        }

        if (requestParams.containsKey("sortType")) {
            List<String> sortTypeList = requestParams.get("sortType");
            orders = getOrderListOfSort(root, sortTypeList);
        }

        return findAllFilteredAndSorted(criteriaQuery, root, predicates, orders);

    }

    @Override
    public List<M> findAllFiltered(CriteriaQuery<M> cr, Root<M> root, List<Predicate> predicates) {
        return findAllFilteredAndSorted(cr, root, predicates, Collections.emptyList());
    }

    @Override
    public M findOneFiltered(CriteriaQuery<M> cr, Root<M> root,
                             List<Predicate> predicates) {
        cr
                .select(root)
                .where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cr).setMaxResults(1).getSingleResult();
    }

    @Override
    public <T> M findOneFiltered(CriteriaQuery<M> cr, Join<T, M> join,
                             List<Predicate> predicates) {
        cr
                .select(join)
                .where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cr).getSingleResult();
    }

    @Override
    public List<M> findAllSorted(CriteriaQuery<M> cr, Root<M> root, List<Order> orders) {
        return findAllFilteredAndSorted(cr, root, Collections.emptyList(), orders);
    }

    @Override
    public List<M> findAllSortedByType(String sortType) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        List<Order> orderList = getOrderListOfSort(root, List.of(sortType));

        return findAllFilteredAndSorted(criteriaQuery, root, Collections.emptyList(), orderList);
    }

    @Override
    public List<M> findAllSortedByType(List<String> sortType) {
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<M> root = criteriaQuery.from(clazz);

        List<Order> orderList = getOrderListOfSort(root, sortType);

        return findAllFilteredAndSorted(criteriaQuery, root, Collections.emptyList(), orderList);
    }

    @Override
    public List<M> findAllFilteredAndSorted(CriteriaQuery<M> cr, Root<M> root,
                                            List<Predicate> predicates, List<Order> orders) {
        cr
            .select(root)
            .where(predicates.toArray(new Predicate[0]))
            .orderBy(orders);

        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public <T> List<M> findAllFilteredAndSorted(CriteriaQuery<M> cr, Join<T, M> join,
                                                List<Predicate> predicates, List<Order> orders) {
        cr
                .select(join)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(orders);

        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public void delete(M model) {
        entityManager.remove(model);
    }

    @Override
    public void deleteById(Long id) {
        M model = findById(id);
        delete(model);
    }

    @Override
    public void create(M newModel) {
        entityManager.persist(newModel);
    }

    @Override
    public void update(M changedModel) {
        entityManager.merge(changedModel);
    }

    @Override
    public Integer size() {
        return findAll().size();
    }

    @Override
    public boolean isExist(M model) {
        return entityManager.contains(model);
    }
}
