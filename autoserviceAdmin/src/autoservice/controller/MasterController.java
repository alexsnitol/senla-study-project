package autoservice.controller;

import autoservice.repository.model.Master;
import autoservice.service.IMasterService;

import java.util.List;

public class MasterController extends AbstractController<Master, IMasterService> {
    private IMasterService masterService;

    public MasterController(IMasterService defaultService) {
        super(defaultService);
        this.masterService = defaultService;
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
}
