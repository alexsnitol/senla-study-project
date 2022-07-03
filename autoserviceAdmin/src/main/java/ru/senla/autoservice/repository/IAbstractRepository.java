package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.AbstractModel;

import java.util.List;

public interface IAbstractRepository<M extends AbstractModel> {

    void setRepository(List<M> repository);
    List<M> findAll();
    List<M> findAllSorted(String sortType);
    M findById(Long id);
    void delete(M model);
    void deleteById(Long id);
    void create(M newModel);
    void update(M changedModel);
    Integer size();
    boolean isExist(M model);

}
