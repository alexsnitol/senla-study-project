package ru.senla.autoservice.controller;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.util.JsonUtil;

import java.io.IOException;
import java.util.List;

@Singleton
@Slf4j
public class MasterController extends AbstractController<Master, IMasterService> {

    private static MasterController instance;
    @Autowired
    private IMasterService masterService;

    @PostConstruct
    public void setInstance() {
        instance = this;
    }

    public static MasterController getInstance() {
        return instance;
    }

    @PostConstruct
    public void init() {
        this.defaultService = masterService;
    }

    public void setMasterService(IMasterService masterService) {
        this.defaultService = masterService;
        this.masterService = masterService;
    }

    public void deleteById(Long masterId) {
        log.info("Deleting master with id {}", masterId);
        masterService.deleteById(masterId);
    }

    public void add(Master newMaster) {
        log.info("Adding new master with id {}", newMaster.getId());
        masterService.add(newMaster);
    }

    public List<Master> getMastersByOrder(Long orderId) {
        return masterService.getMastersByOrder(orderId);
    }

    public String getFullName(Master master) {
        return masterService.getFullName(master);
    }

    public String getFullNameWithId(Master master) {
        return masterService.getFullNameWithId(master);
    }

    public List<Master> getSorted(String sortType) {
        return masterService.getSorted(sortType);
    }

    public List<Master> getSorted(List<Master> listOfMaster, String sortType) {
        return masterService.getSorted(listOfMaster, sortType);
    }

    public void exportMasterToJsonFile(Long masterId, String fileName) throws IOException {
        log.info("Export master with id {} to json file: {}", masterId, fileName);
        masterService.exportMasterToJsonFile(masterId, fileName);
    }

    public void importMasterFromJsonFile(String path) throws IOException {
        log.info("Import master from json file: {}", path);
        masterService.importMasterFromJsonFile(path);
    }

    public void exportAllMastersToJsonFile() throws IOException {
        log.info("Export all masters to json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "masterList.json");
        masterService.exportAllMastersToJsonFile();
    }

//    @PostConstruct
    public void importAllMastersFromJsonFile() throws IOException {
        log.info("Import all masters from json file: {}", JsonUtil.JSON_CONFIGURATION_PATH + "masterList.json");
        masterService.importAllMastersFromJsonFile();
    }

}
