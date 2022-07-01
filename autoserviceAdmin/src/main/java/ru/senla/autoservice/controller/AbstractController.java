package ru.senla.autoservice.controller;

import ru.senla.autoservice.repository.model.AbstractModel;
import ru.senla.autoservice.service.IAbstractService;

import java.util.List;

public abstract class AbstractController<M extends AbstractModel, S extends IAbstractService<M>> {

    protected S defaultService;

    public AbstractController() {
    }

    protected AbstractController(S defaultService) {
        this.defaultService = defaultService;
    }

    public M getById(Long id) throws Exception {
        return this.defaultService.getById(id);
    }

    public List<M> getAll() {
        return defaultService.getAll();
    }

    public void update(M changedModel) {
        defaultService.update(changedModel);
    }

    public Integer size() {
        return this.defaultService.size();
    }

}
