package autoservice.service.impl;

import autoservice.repository.IAbstractRepository;
import autoservice.repository.model.AbstractModel;
import autoservice.repository.model.Master;
import autoservice.service.IAbstractService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServiceImpl<M extends AbstractModel, R extends IAbstractRepository<M>> implements IAbstractService<M> {

    protected R defaultRepository = null;


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
        this.defaultRepository.deleteById(id);
    }

    public void add(M model) {
        this.defaultRepository.add(model);
    }

    public void update(M oldModel, M newModel) {
        defaultRepository.update(oldModel, newModel);
    }

    public Integer size() {
        return this.defaultRepository.size();
    }

}
