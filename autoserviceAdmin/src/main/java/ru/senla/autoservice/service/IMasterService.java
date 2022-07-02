package ru.senla.autoservice.service;

import ru.senla.autoservice.repository.IMasterRepository;
import ru.senla.autoservice.repository.IOrderRepository;
import ru.senla.autoservice.repository.model.Master;

import java.io.IOException;
import java.util.List;

public interface IMasterService extends IAbstractService<Master> {

    void setMasterRepository(IMasterRepository masterRepository);

    void setOrderRepository(IOrderRepository orderRepository);

    List<Master> getMastersByOrderId(Long orderId);

    String getFullName(Master master);

    String getFullNameWithId(Master master);

    void exportMasterToJsonFile(Long masterId, String fileName) throws IOException;

    void importMasterFromJsonFile(String path) throws IOException;

    void exportAllMastersToJsonFile() throws IOException;

    void importAllMastersFromJsonFile() throws IOException;

}
