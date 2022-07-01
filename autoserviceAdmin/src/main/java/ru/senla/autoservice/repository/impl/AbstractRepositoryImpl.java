package ru.senla.autoservice.repository.impl;

import jakarta.persistence.EntityManager;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import ru.senla.autoservice.repository.IAbstractRepository;
import ru.senla.autoservice.repository.model.AbstractModel;
import ru.senla.autoservice.util.HibernateSessionFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryImpl<M extends AbstractModel> implements IAbstractRepository<M> {

    protected List<M> repository = new ArrayList<>();
    protected EntityManager entityManager = HibernateSessionFactoryUtil.createEntityManager();
    protected Class<M> clazz;

    public void setRepository(List<M> repository) {
        this.repository = repository;
    }

    public void setClazz(final Class<M> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<M> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    public M findById(Long id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public void delete(M model) {
        entityManager.remove(model);
    }

    @Override
    public void deleteById(Long id) {
        M model = findById(id);
        delete(model);
    }

    @Override
    public M create(M newModel) {
        entityManager.persist(newModel);
        return newModel;
    }

    @Override
    public void update(M changedModel) {
        entityManager.merge(changedModel);
    }

    @Override
    public Integer size() {
        return findAll().size();
    }

    @Override
    public boolean isExist(M model) {
        return entityManager.contains(model);
    }
}
