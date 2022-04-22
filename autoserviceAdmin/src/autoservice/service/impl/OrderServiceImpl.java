package autoservice.service.impl;

import autoservice.repository.IGarageRepository;
import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.impl.OrderRepositoryImpl;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.service.IOrderService;
import autoservice.service.comparator.MapOrderComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl extends AbstractServiceImpl<Order, IOrderRepository> implements IOrderService {

    private IOrderRepository orderRepository;
    private IMasterRepository masterRepository;
    private IGarageRepository garageRepository;

    public OrderServiceImpl() {
        super(new OrderRepositoryImpl());
    }

    public OrderServiceImpl(IOrderRepository defaultRepository, IMasterRepository masterRepository, IGarageRepository garageRepository) {
        super(defaultRepository);
        this.orderRepository = defaultRepository;
        this.masterRepository = masterRepository;
        this.garageRepository = garageRepository;
    }

    public void setOrderRepository(IOrderRepository orderRepository) {
        this.defaultRepository = orderRepository;
        this.orderRepository = orderRepository;
    }

    public void setMasterRepository(IMasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    public void setGarageRepository(IGarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        Order orderById = orderRepository.getById(orderId);

        if (orderById.getTimeOfBegin() == null) {
            return;
        }

        orderById.setTimeOfCompletion(orderById.getTimeOfBegin().plusMinutes(minutes));
    }

    public void setStatus(Long orderId, OrderStatusEnum newStatus) {
        Order order = orderRepository.getById(orderId);
        order.setStatus(newStatus);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = masterRepository.getById(masterId);

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
            }

            orderById.getListOfMastersId().add(masterId);
        }
    }

    public void removeMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = null;

        for (Long tmpMasterId : orderById.getListOfMastersId()) {
            if (tmpMasterId.equals(masterId)) {
                masterById = masterRepository.getById(tmpMasterId);
                break;
            }
        }

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() - 1);
            }

            orderById.getListOfMastersId().remove(masterId);
        }
    }

    private void shiftTimeOfCompletionOneOrder(Order order, int shiftMinutes) {
        if (order.getStatus() == OrderStatusEnum.IN_PROCESS) {
            order.setTimeOfCompletion(order.getTimeOfCompletion().plusMinutes(shiftMinutes));
        } else if (order.getStatus() == OrderStatusEnum.POSTPONED) {
            order.setTimeOfBegin(order.getTimeOfBegin().plusMinutes(shiftMinutes));
            order.setTimeOfCompletion(order.getTimeOfCompletion().plusMinutes(shiftMinutes));
        }
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        Order orderById = orderRepository.getById(orderId);

        if (orderById == null)
            return;

        if (orderById.getStatus() != OrderStatusEnum.IN_PROCESS)
            return;

        shiftTimeOfCompletionOneOrder(orderById, shiftMinutes);

        List<Long> listOfMastersIdByOrder = orderById.getListOfMastersId();

        for (Order order : orderRepository.getAll()) {
            if (order.getStatus() == OrderStatusEnum.POSTPONED) {
                for (Long masterId : listOfMastersIdByOrder) {
                    if (order.getListOfMastersId().stream().filter(m -> m.equals(masterId)).findFirst().orElse(null) != null) {
                        shiftTimeOfCompletionOneOrder(order, shiftMinutes);
                        break;
                    }
                }
            }
        }
    }

    public void setPrice(Long orderId, float price) {
        orderRepository.getById(orderId).setPrice(price);
    }

    public String getInfoOfOrder(Order order) {
        return "id: " + order.getId()
                + "\nprice: " + order.getPrice()
                + "\nstatus: " + order.getStatus()
                + "\ntime of created: " + order.getTimeOfCreated().toString()
                + "\ntime of begin: " + order.getTimeOfBegin().toString()
                + "\ntime of completion: " + order.getTimeOfCompletion().toString();
    }

    @Override
    public List<Order> getSorted(String sortType) {
        return getSorted(orderRepository.getAll(), sortType);
    }

    @Override
    public List<Order> getSorted(List<Order> orders, String sortType) {
        List<Order> sortedOrders = new ArrayList<>(orders);
        MapOrderComparator mapOrderComparator = new MapOrderComparator();

        sortedOrders.sort(mapOrderComparator.exetuce(sortType));

        return sortedOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByDateTime(LocalDateTime from, LocalDateTime to) {
        return getOrdersFilteredByDateTime(this.orderRepository.getAll(), from, to);
    }

    @Override
    public List<Order> getOrdersFilteredByDateTime(List<Order> orders, LocalDateTime from, LocalDateTime to) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().isAfter(from) && o.getTimeOfCompletion().isBefore(to))
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByStatus(OrderStatusEnum status) {
        return getOrdersFilteredByStatus(this.orderRepository.getAll(), status);
    }

    @Override
    public List<Order> getOrdersFilteredByStatus(List<Order> orders, OrderStatusEnum status) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    @Override
    public List<Order> getOrdersFilteredByMaster(Long masterId) {
        return getOrdersFilteredByMaster(this.orderRepository.getAll(), masterId);
    }

    @Override
    public List<Order> getOrdersFilteredByMaster(List<Order> orders, Long masterId) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getListOfMastersId().stream().filter(m -> m.equals(masterId)).findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        return filteredOrders;
    }

    public void exportOrderToJsonFile(Long orderId, String fileName) throws IOException {
        Order orderById = getById(orderId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(new File(fileName + ".json"), orderById);
    }

    public void importOrderFromJsonFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonNode orderJsonNode = objectMapper.readTree(new File(path));
        Order orderJson = objectMapper.readValue(new File(path), Order.class);

        Order orderByJsonId = getById(orderJsonNode.get("id").asLong());

        if (orderByJsonId != null) {
            orderRepository.update(orderByJsonId, orderJson);
        } else {
            add(orderJson);
        }
    }

}
