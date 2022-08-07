package ru.senla.autoservice.service.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.AbstractModel;
import ru.senla.autoservice.repo.IAbstractRepository;
import ru.senla.autoservice.service.IAbstractService;
import ru.senla.autoservice.service.helper.EntityHelper;
import ru.senla.autoservice.util.JsonUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor
public abstract class AbstractServiceImpl<M extends AbstractModel, R extends IAbstractRepository<M>>
        implements IAbstractService<M> {

    protected R defaultRepository = null;
    @PersistenceContext
    protected EntityManager entityManager;
    protected Class<M> clazz;


    @Autowired
    protected AbstractServiceImpl(R defaultRepository) {
        this.defaultRepository = defaultRepository;
    }

    public void setClazz(final Class<M> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet, null);
    }

    public M getById(Long id) {
        M model = this.defaultRepository.findById(id);
        EntityHelper.checkEntity(model, clazz, id);
        return model;
    }

    public List<M> getAll() {
        return defaultRepository.findAll();
    }

    public List<M> getAll(MultiValueMap<String, String> requestParams) {
        return defaultRepository.findAll(requestParams);
    }

    @Transactional
    public void delete(M model) {
        this.defaultRepository.delete(model);
        log.info("Model {} with id {} successful deleted", model.getClass(), model.getId());
    }

    @Transactional
    public void deleteById(Long id) {
        M model = this.defaultRepository.findById(id);
        EntityHelper.checkEntity(model, clazz, id);

        this.defaultRepository.delete(model);
        log.info("Model with id {} successful deleted", id);
    }

    @Transactional
    public M add(M newModel) {
        this.defaultRepository.create(newModel);
        log.info("Model {} with id {} successful added", newModel.getClass(), newModel.getId());
        return newModel;
    }

    @Transactional
    public M update(M changedModel) {
        defaultRepository.update(changedModel);
        log.info("Model {} with id {} successful updated on model with id {}",
                changedModel.getClass(), changedModel.getId(), changedModel.getId());
        return changedModel;
    }

    public Integer size() {
        return this.defaultRepository.size();
    }

    public List<M> getSorted(String sortType) {
        return defaultRepository.findAllSortedByType(sortType);
    }

    @Transactional
    @Override
    public void importModelFromJsonFile(String path) throws IOException {
        M modelJson = JsonUtil.importModelFromJsonFile(clazz, path);

        if (defaultRepository.isExist(modelJson)) {
            update(modelJson);
        } else {
            add(modelJson);
        }

        log.info("{} successful imported", clazz.getSimpleName());
    }

    @Transactional
    @Override
    public void importAllFromJsonFile(String path) throws IOException {
        List<M> modelList = JsonUtil.importModelListFromJsonFile(clazz, path);

        modelList.forEach(garage -> {
            if (defaultRepository.isExist(garage)) {
                update(garage);
            } else {
                add(garage);
            }
        });

        log.info("All garages successful imported");
    }

    @Override
    public void exportModelToJsonFile(Long id, String fileName) throws IOException {
        JsonUtil.exportModelToJsonFile(getById(id), fileName);
        log.info("{} with id {} successful exported", clazz.getSimpleName(), id);
    }

    @Override
    public void exportAllToJsonFile(String path) throws IOException {
        JsonUtil.exportModelListToJsonFile(defaultRepository.findAll(), path);
        log.info("List of {} successful exported", clazz.getSimpleName());
    }

}
