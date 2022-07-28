package ru.senla.autoservice.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
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
        this.clazz = Master.class;
        this.defaultRepository = masterRepository;
    }


    @Override
    public List<Order> getOrdersByMasterId(Long id) {
        Master master = getById(id);
        return master.getOrders();
    }

    @Override
    public List<Master> getAllByOrderId(String orderIdStr, MultiValueMap<String, String> requestParams) {
        if (requestParams.containsKey("orderId")) {
            requestParams.set("orderId", orderIdStr);
        } else {
            requestParams.add("orderId", orderIdStr);
        }
        return getAll(requestParams);
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

    @Override
    public List<Master> checkRequestParamsAndGetAll(MultiValueMap<String, String> requestParams) {
        requestParams.remove("orderId");
        return getAll(requestParams);
    }

    public void exportMasterToJsonFile(Long masterId, String fileName) throws IOException {
        Master masterById = getById(masterId);
        JsonUtil.exportModelToJsonFile(masterById, fileName);
        log.info("Master with id {} successful exported", masterId);
    }

    public void exportAllMastersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(masterRepository.findAll(),
                JsonUtil.JSON_CONFIGURATION_PATH + "masterList");
        log.info("All masters successful exported");
    }

}
