package ru.senla.autoservice.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.comparator.MapMasterComparator;
import ru.senla.autoservice.util.JsonUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
@Service
public class MasterServiceImpl extends AbstractServiceImpl<Master, IMasterRepository> implements IMasterService {

    private final IMasterRepository masterRepository;
    private final IOrderRepository orderRepository;

    @Autowired
    public MasterServiceImpl(IMasterRepository masterRepository, IOrderRepository orderRepository) {
        this.masterRepository = masterRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void init() {
        this.defaultRepository = masterRepository;
    }

    public List<Master> getMastersByOrderId(Long orderId) {
        return orderRepository.findById(orderId).getMasters();
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

        if (masterRepository.isExist(masterJson)) {
            update(masterJson);
        } else {
            add(masterJson);
        }
        log.info("Master successful imported");
    }

    public void exportAllMastersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(masterRepository.findAll(),
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
