package ru.senla.autoservice.service;

import java.util.List;

public interface IAbstractService<M> {

    M getById(Long id);

    List<M> getAll();

    void deleteById(Long id);

    void add(M model);

    void update(M oldModel, M newModel);

    Integer size();

    List<M> getSorted(String sortType);

    List<M> getSorted(List<M> listOfModel, String sortType);

}
