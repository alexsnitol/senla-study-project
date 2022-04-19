package autoservice.repository.impl;

import autoservice.repository.IAbstractRepository;
import autoservice.repository.model.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryImpl<M extends AbstractModel> implements IAbstractRepository<M> {
    protected List<M> repository = new ArrayList<>();

    @Override
    public List<M> getAll() {
        return repository;
    }

    @Override
    public M getById(Long id) {
        return repository.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        M entity = getById(id);
        repository.remove(entity);
    }

    @Override
    public void add(M model) {
        repository.add(model);
    }

    public void update(M oldModel, M newModel) {
        repository.set(repository.indexOf(oldModel), newModel);
    }

    @Override
    public Integer size() {
        return repository.size();
    }
}
