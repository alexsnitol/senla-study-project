package autoservice.service;

import autoservice.repository.model.Master;

import java.util.List;

public interface IMasterService extends IAbstractService<Master> {
    List<Master> getMastersByOrder(Long idOrder);
    String getFullName(Master master);
    String getFullNameWithId(Master master);
}
