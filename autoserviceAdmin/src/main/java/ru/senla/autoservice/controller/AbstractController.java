package ru.senla.autoservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.AbstractModel;
import ru.senla.autoservice.service.IAbstractService;

import java.util.List;

@Slf4j
public abstract class AbstractController<M extends AbstractModel, S extends IAbstractService<M>> {

    protected S defaultService;
    protected Class<M> clazz;

    public AbstractController() {
    }

    protected AbstractController(S defaultService) {
        this.defaultService = defaultService;
    }

    public List<M> getAll() {
        return defaultService.getAll();
    }

    public List<M> getAll(MultiValueMap<String, String> requestParams) {
        return defaultService.getAll(requestParams);
    }

    public M getById(Long id) {
        return this.defaultService.getById(id);
    }

    public M add(M newModel) {
        defaultService.add(newModel);
        return newModel;
    }

    public M update(Long id, M changedModel) {
        changedModel.setId(id);
        defaultService.update(changedModel);
        return changedModel;
    }

    public ResponseEntity<String> delete(M model) {
        log.info("Deleting model {}", model);
        defaultService.delete(model);

        return ResponseEntity.ok(clazz.getSimpleName() + " with id " + model.getId() + " was deleted");
    }

    public ResponseEntity<String> deleteById(Long id) {
        log.info("Deleting model with id {}", id);
        defaultService.deleteById(id);

        return ResponseEntity.ok(clazz.getSimpleName() + " with id " + id + " was deleted");
    }

    public Integer size() {
        return this.defaultService.size();
    }

}
