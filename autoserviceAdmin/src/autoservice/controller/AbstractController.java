package autoservice.controller;

import autoservice.repository.model.AbstractModel;
import autoservice.service.IAbstractService;
import configuremodule.annotation.Autowired;

import java.util.List;

public abstract class AbstractController<M extends AbstractModel, S extends IAbstractService<M>> {

    protected S defaultService;

    public AbstractController() {}
    protected AbstractController(S defaultService) {
        this.defaultService = defaultService;
    }

    public M getById(Long id) {
        return this.defaultService.getById(id);
    }

    public List<M> getAll() {
        return defaultService.getAll();
    }

    public void update(M oldModel, M newModel) {defaultService.update(oldModel, newModel);}

    public Integer size() {
        return this.defaultService.size();
    }

}
