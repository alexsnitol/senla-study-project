package ru.senla.autoservice.repository.impl;

import ru.senla.autoservice.repository.IAbstractRepository;
import ru.senla.autoservice.repository.model.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryImpl<M extends AbstractModel> implements IAbstractRepository<M> {

    protected List<M> repository = new ArrayList<>();

    public void setRepository(List<M> repository) {
        this.repository = repository;
    }

    @Override
    public List<M> getAll() {
        return repository;
    }

    @Override
    public M getById(Long id) {
        return repository.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        M entity = getById(id);
        return repository.remove(entity);
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
