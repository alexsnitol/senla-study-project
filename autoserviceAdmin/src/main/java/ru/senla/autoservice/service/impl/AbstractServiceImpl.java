package ru.senla.autoservice.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IAbstractRepository;
import ru.senla.autoservice.repository.model.AbstractModel;
import ru.senla.autoservice.service.IAbstractService;
import ru.senla.autoservice.util.HibernateSessionFactoryUtil;

import java.util.List;

@Slf4j
@NoArgsConstructor
public abstract class AbstractServiceImpl<M extends AbstractModel, R extends IAbstractRepository<M>>
        implements IAbstractService<M> {

    protected R defaultRepository = null;

    protected AbstractServiceImpl(R defaultRepository) {
        this.defaultRepository = defaultRepository;
    }

    public M getById(Long id) {
        return this.defaultRepository.findById(id);
    }

    public List<M> getAll() {
        return defaultRepository.findAll();
    }

    public void delete(M model) {
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            this.defaultRepository.delete(model);

            transaction.commit();

            log.info("Model {} with id {} successful deleted", model.getClass(), model.getId());
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    public void deleteById(Long id) {
        M model = this.defaultRepository.findById(id);

        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            this.defaultRepository.deleteById(id);

            transaction.commit();

            log.info("Model {} with id {} successful deleted", model.getClass(), id);
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    public M add(M newModel) {
        M createdModel = null;

        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            createdModel = this.defaultRepository.create(newModel);

            transaction.commit();

            log.info("Model {} with id {} successful added", createdModel.getClass(), createdModel.getId());
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        return createdModel;
    }

    public void update(M changedModel) {
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            defaultRepository.update(changedModel);

            transaction.commit();

            log.info("Model {} with id {} successful updated on model with id {}",
                    changedModel.getClass(), changedModel.getId(), changedModel.getId());
        } catch (Exception e) {
            log.error(e.toString());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    public Integer size() {
        return this.defaultRepository.size();
    }

    public List<M> getSorted(String sortType) {
        return defaultRepository.findAllSorted(sortType);
    }

    public EntityManager createEntityManager() {
        return HibernateSessionFactoryUtil.createEntityManager();
    }

}
