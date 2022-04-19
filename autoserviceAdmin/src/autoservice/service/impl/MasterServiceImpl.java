package autoservice.service.impl;

import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.model.Master;
import autoservice.service.IMasterService;
import autoservice.service.comparator.MapMasterComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MasterServiceImpl extends AbstractServiceImpl<Master, IMasterRepository> implements IMasterService {
    private IMasterRepository masterRepository;
    private IOrderRepository orderRepository;

    public MasterServiceImpl(IMasterRepository defaultRepository, IOrderRepository orderRepository) {
        super(defaultRepository);
        this.masterRepository = defaultRepository;
        this.orderRepository = orderRepository;
    }

    public List<Master> getMastersByOrder(Long idOrder) {
        return orderRepository.getById(idOrder).getMasters();
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
}
