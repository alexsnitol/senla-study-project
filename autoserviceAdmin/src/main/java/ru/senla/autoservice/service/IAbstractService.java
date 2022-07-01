package ru.senla.autoservice.service;

import java.util.List;

public interface IAbstractService<M> {

    M getById(Long id) throws Exception;
    List<M> getAll();
    void delete(M model);
    void deleteById(Long id);
    M add(M newModel);
    void update(M changedModel);
    Integer size();
    List<M> getSorted(String sortType);
    List<M> getSorted(List<M> listOfModel, String sortType);

}
