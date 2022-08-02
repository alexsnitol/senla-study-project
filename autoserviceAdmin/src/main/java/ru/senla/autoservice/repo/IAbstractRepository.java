package ru.senla.autoservice.repo;

import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

public interface IAbstractRepository<M> {

    M findOne(MultiValueMap<String, String> requestParams);
    M findOneFiltered(CriteriaQuery<M> cr, Root<M> root, List<Predicate> predicates);
    <T> M findOneFiltered(CriteriaQuery<M> cr, Join<T, M> join, List<Predicate> predicates);
    List<M> findAll();
    List<M> findAll(MultiValueMap<String, String> requestParams);
    List<M> findAllFiltered(CriteriaQuery<M> cr, Root<M> root, List<Predicate> predicates);
    List<M> findAllSorted(CriteriaQuery<M> cr, Root<M> root, List<Order> orders);
    List<M> findAllSortedByType(String sortType);
    List<M> findAllSortedByType(List<String> sortType);
    List<M> findAllFilteredAndSorted(CriteriaQuery<M> cr, Root<M> root,
                                     List<Predicate> predicates, List<Order> orders);
    <T> List<M> findAllFilteredAndSorted(CriteriaQuery<M> cr, Join<T, M> join,
                                         List<Predicate> predicates, List<Order> orders);

    Map<String, List<Order>> getSortMap(Root<M> root);
    List<Order> getOrderListOfSort(Root<M> root, List<String> sortType);
    List<Predicate> getPredicateList(Root<M> root, MultiValueMap<String, String> requestParams);

    M findById(Long id);

    void delete(M model);
    void deleteById(Long id);
    void create(M newModel);
    void update(M changedModel);
    Integer size();
    boolean isExist(M model);

}
