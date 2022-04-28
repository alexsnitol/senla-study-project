package autoservice.repository;

import autoservice.repository.model.AbstractModel;

import java.util.List;

public interface IAbstractRepository<M extends AbstractModel> {

    List<M> getAll();
    M getById(Long id);
    void deleteById(Long id);
    void add(M model);
    void update(M oldModel, M newModel);
    Integer size();

}
