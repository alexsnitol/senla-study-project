package ru.senla.autoservice.repository.impl;

import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import ru.senla.autoservice.repository.IAbstractRepository;
import ru.senla.autoservice.repository.model.AbstractModel;
import ru.senla.autoservice.util.EntityManagerUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryImpl<M extends AbstractModel> implements IAbstractRepository<M> {

    protected List<M> repository = new ArrayList<>();

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
        return EntityManagerUtil.getEntityManager().createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    public M findById(Long id) {
        return EntityManagerUtil.getEntityManager().find(clazz, id);
    }

    @Override
    public void delete(M model) {
        EntityManagerUtil.getEntityManager().remove(model);
    }

    @Override
    public void deleteById(Long id) {
        M model = findById(id);
        delete(model);
    }

    @Override
    public void create(M newModel) {
        EntityManagerUtil.getEntityManager().persist(newModel);
    }

    @Override
    public void update(M changedModel) {
        EntityManagerUtil.getEntityManager().merge(changedModel);
    }

    @Override
    public Integer size() {
        return findAll().size();
    }

    @Override
    public boolean isExist(M model) {
        return EntityManagerUtil.getEntityManager().contains(model);
    }
}
