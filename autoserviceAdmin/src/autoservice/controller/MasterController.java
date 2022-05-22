package autoservice.controller;

import autoservice.repository.model.Master;
import autoservice.service.IMasterService;
import autoservice.service.impl.MasterServiceImpl;
import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;

import java.io.IOException;
import java.util.List;

@Singleton
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
        masterService.deleteById(masterId);
    }

    public void add(Master newMaster) {
        masterService.add(newMaster);
    }

    public List<Master> getMastersByOrder(Long idOrder) {
        return masterService.getMastersByOrder(idOrder);
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
        masterService.exportMasterToJsonFile(masterId, fileName);
    }

    public void importMasterFromJsonFile(String path) throws IOException {
        masterService.importMasterFromJsonFile(path);
    }

    public void exportAllMastersToJsonFile() throws IOException {
        masterService.exportAllMastersToJsonFile();
    }

    @PostConstruct
    public void importAllMastersFromJsonFile() throws IOException {
        masterService.importAllMastersFromJsonFile();
    }

}
