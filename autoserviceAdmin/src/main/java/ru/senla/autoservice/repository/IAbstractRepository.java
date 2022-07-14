package ru.senla.autoservice.repository;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import ru.senla.autoservice.repository.model.AbstractModel;

import java.util.List;

public interface IAbstractRepository<M extends AbstractModel> {

    void setRepository(List<M> repository);
    List<M> findAll();
    List<M> findAllSorted(String sortType);
    void sortCriteriaQuery(CriteriaQuery<M> cr, Root<M> root, String sortType);
    M findById(Long id);
    void delete(M model);
    void deleteById(Long id);
    void create(M newModel);
    void update(M changedModel);
    Integer size();
    boolean isExist(M model);

}
