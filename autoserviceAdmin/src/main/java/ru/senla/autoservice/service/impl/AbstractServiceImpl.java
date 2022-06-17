package ru.senla.autoservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IAbstractRepository;
import ru.senla.autoservice.repository.model.AbstractModel;
import ru.senla.autoservice.service.IAbstractService;

import java.util.List;

@Slf4j
public abstract class AbstractServiceImpl<M extends AbstractModel, R extends IAbstractRepository<M>>
        implements IAbstractService<M> {

    protected R defaultRepository = null;


    public AbstractServiceImpl() {
    }

    protected AbstractServiceImpl(R defaultRepository) {
        this.defaultRepository = defaultRepository;
    }

    public M getById(Long id) {
        return this.defaultRepository.getById(id);
    }

    public List<M> getAll() {
        return defaultRepository.getAll();
    }

    public void deleteById(Long id) {
        M model = this.defaultRepository.getById(id);
        boolean result = this.defaultRepository.deleteById(id);
        if (result) {
            log.info("Model {} with id {} successful deleted", model.getClass(), id);
        } else {
            log.error("Model {} with id {} not exist", model.getClass(), id);
        }
    }

    public void add(M model) {
        this.defaultRepository.add(model);
        log.info("Model {} with id {} successful added", model.getClass(), model.getId());
    }

    public void update(M oldModel, M newModel) {
        defaultRepository.update(oldModel, newModel);
        log.info("Model {} with id {} successful updated on model with id {}",
                newModel.getClass(), oldModel.getId(), newModel.getId());
    }

    public Integer size() {
        return this.defaultRepository.size();
    }

}
