package ru.senla.autoservice.service.impl;

import configuremodule.annotation.Autowired;
import configuremodule.annotation.PostConstruct;
import configuremodule.annotation.Singleton;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.comparator.MapMasterComparator;
import ru.senla.autoservice.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Setter
@Slf4j
public class MasterServiceImpl extends AbstractServiceImpl<Master, IMasterRepository> implements IMasterService {

    @Autowired
    private IMasterRepository masterRepository;
    @Autowired
    private IOrderRepository orderRepository;


    @PostConstruct
    public void init() {
        this.defaultRepository = masterRepository;
    }

    public void setMasterRepository(IMasterRepository masterRepository) {
        this.defaultRepository = masterRepository;
        this.masterRepository = masterRepository;
    }

    public List<Master> getMastersByOrder(Long orderId) {
        List<Long> listOfMastersId = orderRepository.getById(orderId).getListOfMastersId();
        List<Master> mastersByOrder = new ArrayList<>();

        for (Master master : getAll()) {
            if (listOfMastersId.contains(master.getId())) {
                mastersByOrder.add(master);
            }
        }

        return mastersByOrder;
    }

    @Override
    public List<Master> getSorted(String sortType) {
        return getSorted(masterRepository.getAll(), sortType);
    }

    @Override
    public List<Master> getSorted(List<Master> masters, String sortType) {
        List<Master> sortedMasters = new ArrayList<>(masters);
        MapMasterComparator mapMasterComparator = new MapMasterComparator();

        sortedMasters.sort(mapMasterComparator.exetuce(sortType));

        return sortedMasters;
    }

    public String getFullName(Master master) {
        return master.getLastName() + " " + master.getFirstName() + " " + master.getPatronymic();
    }

    public String getFullNameWithId(Master master) {
        return getFullName(master) + " [id: " + master.getId() + "]";
    }

    public void exportMasterToJsonFile(Long masterId, String fileName) throws IOException {
        Master masterById = getById(masterId);
        JsonUtil.exportModelToJsonFile(masterById, fileName);
        log.info("Master with id {} successful exported", masterId);
    }

    public void importMasterFromJsonFile(String path) throws IOException {
        Master masterJson = JsonUtil.importModelFromJsonFile(new Master(), path);
        Master masterByJsonId = getById(masterJson.getId());

        if (masterByJsonId != null) {
            update(masterByJsonId, masterJson);
        } else {
            add(masterJson);
        }
        log.info("Master successful imported");
    }

    public void exportAllMastersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(masterRepository.getAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "masterList");
        log.info("All masters successful exported");
    }

    public void importAllMastersFromJsonFile() throws IOException {
        List<Master> masterList = JsonUtil.importModelListFromJsonFile(new Master(),
                JsonUtil.JSON_CONFIGURATION_PATH + "masterList.json");
        masterRepository.setRepository(masterList);
        log.info("All masters successful imported");
    }
}
