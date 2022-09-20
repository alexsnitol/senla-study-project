package ru.senla.autoservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import ru.senla.autoservice.model.Master;
import ru.senla.autoservice.model.Order;
import ru.senla.autoservice.repo.IMasterRepository;
import ru.senla.autoservice.repo.IOrderRepository;
import ru.senla.autoservice.service.IMasterService;
import ru.senla.autoservice.service.helper.EntityHelper;

import javax.annotation.PostConstruct;
import java.util.List;

@Setter
@Slf4j
@AllArgsConstructor
@Service
public class MasterServiceImpl extends AbstractServiceImpl<Master, IMasterRepository> implements IMasterService {

    private final IMasterRepository masterRepository;
    private final IOrderRepository orderRepository;


    @PostConstruct
    public void init() {
        this.clazz = Master.class;
        this.defaultRepository = masterRepository;
    }


    @Override
    public List<Order> getOrdersByMasterId(@NonNull Long id) {
        Master master = getById(id);
        return master.getOrders();
    }

    @Override
    public List<Master> getAllByOrderId(@NonNull String orderIdStr,
                                        @NonNull MultiValueMap<String, String> requestParams) {
        EntityHelper.checkEntityOnNullAfterFindedById(
                orderRepository.findById(Long.valueOf(orderIdStr)), Order.class, Long.valueOf(orderIdStr)
        );
        if (requestParams.containsKey("orderId")) {
            requestParams.set("orderId", orderIdStr);
        } else {
            requestParams.add("orderId", orderIdStr);
        }
        return getAll(requestParams);
    }

    public String getFullName(@NonNull Master master) {
        return master.getLastName() + " " + master.getFirstName() + " " + master.getPatronymic();
    }

    public String getFullNameWithId(@NonNull Master master) {
        return getFullName(master) + " [id: " + master.getId() + "]";
    }

    @Override
    public List<Master> checkRequestParamsAndGetAll(@NonNull MultiValueMap<String, String> requestParams) {
        requestParams.remove("orderId");
        return getAll(requestParams);
    }

}
