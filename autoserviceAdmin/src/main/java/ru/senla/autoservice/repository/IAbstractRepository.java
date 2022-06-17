package ru.senla.autoservice.repository;

import ru.senla.autoservice.repository.model.AbstractModel;

import java.util.List;

public interface IAbstractRepository<M extends AbstractModel> {

    void setRepository(List<M> repository);

    List<M> getAll();

    M getById(Long id);

    boolean deleteById(Long id);

    void add(M model);

    void update(M oldModel, M newModel);

    Integer size();

}
