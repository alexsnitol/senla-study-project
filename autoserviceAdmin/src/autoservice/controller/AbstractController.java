package autoservice.controller;

import autoservice.repository.model.AbstractModel;
import autoservice.service.IAbstractService;

import java.util.List;

public abstract class AbstractController<M extends AbstractModel, S extends IAbstractService<M>> {
    protected S defaultService;

    public AbstractController(S defaultService) {
        this.defaultService = defaultService;
    }

    public M getById(Long id) {
        return this.defaultService.getById(id);
    }

    public List<M> getAll() {
        return defaultService.getAll();
    }

    public void deleteById(Long id) {
        this.defaultService.deleteById(id);
    }

    public void add(M model) {
        this.defaultService.add(model);
    }

    public Integer size() {
        return this.defaultService.size();
    }

    public List<M> getSorted(String sortType) {
        return defaultService.getSorted(sortType);
    }

    public List<M> getSorted(List<M> listOfModel, String sortType) {
        return defaultService.getSorted(listOfModel, sortType);
    }
}
