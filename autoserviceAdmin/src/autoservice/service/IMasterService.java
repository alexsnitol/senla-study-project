package autoservice.service;

import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.model.Master;

import java.io.IOException;
import java.util.List;

public interface IMasterService extends IAbstractService<Master> {

    void setMasterRepository(IMasterRepository masterRepository);
    void setOrderRepository(IOrderRepository orderRepository);

    List<Master> getMastersByOrder(Long idOrder);
    String getFullName(Master master);
    String getFullNameWithId(Master master);

    void exportMasterToJsonFile(Long masterId, String fileName) throws IOException;
    void importMasterFromJsonFile(String path) throws IOException;
    void exportAllMastersToJsonFile() throws IOException;
    void importAllMastersFromJsonFile() throws IOException;

}
