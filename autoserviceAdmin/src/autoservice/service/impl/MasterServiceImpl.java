package autoservice.service.impl;

import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.impl.MasterRepositoryImpl;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.service.IMasterService;
import autoservice.service.comparator.MapMasterComparator;
import autoservice.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MasterServiceImpl extends AbstractServiceImpl<Master, IMasterRepository> implements IMasterService {

    private IMasterRepository masterRepository;
    private IOrderRepository orderRepository;

    public MasterServiceImpl() {
        super(new MasterRepositoryImpl());
    }

    public MasterServiceImpl(IMasterRepository defaultRepository, IOrderRepository orderRepository) {
        super(defaultRepository);
        this.masterRepository = defaultRepository;
        this.orderRepository = orderRepository;
    }

    public void setMasterRepository(IMasterRepository masterRepository) {
        this.defaultRepository = masterRepository;
        this.masterRepository = masterRepository;
    }

    public void setOrderRepository(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    }

    public void importMasterFromJsonFile(String path) throws IOException {
        Master masterJson = JsonUtil.importModelFromJsonFile(new Master(), path);
        Master masterByJsonId = getById(masterJson.getId());

        if (masterByJsonId != null) {
            update(masterByJsonId, masterJson);
        } else {
            add(masterJson);
        }
    }

    public void exportAllMastersToJsonFile() throws IOException {
        JsonUtil.exportModelListToJsonFile(masterRepository.getAll(), JsonUtil.JSON_CONFIGURATION_PATH + "masterList");
    }

    public void importAllMastersFromJsonFile() throws IOException {
        List<Master> masterList = JsonUtil.importModelListFromJsonFile(new Master(), JsonUtil.JSON_CONFIGURATION_PATH + "masterList.json");
        masterRepository.setRepository(masterList);
    }
}
