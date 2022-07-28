package ru.senla.autoservice.service;

import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;

public interface IAbstractService<M> {

    M getById(Long id);
    List<M> getAll();
    List<M> getAll(MultiValueMap<String, String> params);

    void delete(M model);
    void deleteById(Long id);

    M add(M newModel);
    M update(M changedModel);

    Integer size();
    List<M> getSorted(String sortType);
    List<M> getSorted(List<M> listOfModel, String sortType);

    void importModelFromJsonFile(String path) throws IOException;
    void importAllFromJsonFile(String path) throws IOException;
    void exportModelToJsonFile(Long id, String fileName) throws IOException;
    void exportAllToJsonFile(String path) throws IOException;

}
