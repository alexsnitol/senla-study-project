package autoservice.service.impl;

import autoservice.repository.IGarageRepository;
import autoservice.repository.IMasterRepository;
import autoservice.repository.IOrderRepository;
import autoservice.repository.model.Master;
import autoservice.repository.model.Order;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.service.IOrderService;
import autoservice.service.comparator.MapOrderComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl extends AbstractServiceImpl<Order, IOrderRepository> implements IOrderService {

    private IOrderRepository orderRepository;
    private IMasterRepository masterRepository;
    private IGarageRepository garageRepository;

    public OrderServiceImpl(IOrderRepository defaultRepository, IMasterRepository masterRepository, IGarageRepository garageRepository) {
        super(defaultRepository);
        this.orderRepository = defaultRepository;
        this.masterRepository = masterRepository;
        this.garageRepository = garageRepository;
    }

    public void setTimeOfCompletion(Long orderId, int minutes) {
        Order orderById = orderRepository.getById(orderId);

        if (orderById.getTimeOfBegin() == null) {
            return;
        }

        orderById.setTimeOfCompletion((Calendar) orderById.getTimeOfBegin().clone());
        orderById.getTimeOfCompletion().add(Calendar.MINUTE, minutes);
    }

    public void setStatus(Long orderId, OrderStatusEnum status) {
        Order order = orderRepository.getById(orderId);

        if (order.getStatus() != OrderStatusEnum.IN_PROCESS && order.getStatus() != OrderStatusEnum.POSTPONED) {
            if (status == OrderStatusEnum.IN_PROCESS || status == OrderStatusEnum.POSTPONED) {
                for (Master master : order.getMasters()) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() + 1);
                }
            }
        } else {
            if (status != OrderStatusEnum.IN_PROCESS && status != OrderStatusEnum.POSTPONED) {
                for (Master master : order.getMasters()) {
                    master.setNumberOfActiveOrders(master.getNumberOfActiveOrders() - 1);
                }
            }
        }

        order.setStatus(status);
    }

    public void assignMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = masterRepository.getById(masterId);

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() + 1);
            }

            orderById.getMasters().add(masterById);
        }
    }

    public void removeMasterById(Long orderId, Long masterId) {
        Order orderById = orderRepository.getById(orderId);
        Master masterById = null;

        for (Master tmpMaster : orderById.getMasters()) {
            if (tmpMaster.getId().equals(masterId)) {
                masterById = tmpMaster;
                break;
            }
        }

        if (masterById != null) {
            if (orderById.getStatus() == OrderStatusEnum.IN_PROCESS || orderById.getStatus() == OrderStatusEnum.POSTPONED) {
                masterById.setNumberOfActiveOrders(masterById.getNumberOfActiveOrders() - 1);
            }

            orderById.getMasters().remove(masterById);
        }
    }

    private void shiftTimeOfCompletionOneOrder(Order order, int shiftMinutes) {
        if (order.getStatus() == OrderStatusEnum.IN_PROCESS) {
            order.getTimeOfCompletion().add(Calendar.MINUTE, shiftMinutes);
        } else if (order.getStatus() == OrderStatusEnum.POSTPONED) {
            order.getTimeOfBegin().add(Calendar.MINUTE, shiftMinutes);
            order.getTimeOfCompletion().add(Calendar.MINUTE, shiftMinutes);
        }
    }

    public void shiftTimeOfCompletion(Long orderId, int shiftMinutes) {
        Order orderById = orderRepository.getById(orderId);

        if (orderById == null)
            return;

        if (orderById.getStatus() != OrderStatusEnum.IN_PROCESS)
            return;

        shiftTimeOfCompletionOneOrder(orderById, shiftMinutes);

        List<Master> masters = orderById.getMasters();

        for (Order order : orderRepository.getAll()) {
            if (order.getStatus() == OrderStatusEnum.POSTPONED) {
                for (Master master : masters) {
                    if (order.getMasters().stream().filter(m -> m.getId().equals(master.getId())).findFirst().orElse(null) != null) {
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
                + "\ntime of created: " + order.getTimeOfCreated().getTime()
                + "\ntime of begin: " + order.getTimeOfBegin().getTime()
                + "\ntime of completion: " + order.getTimeOfCompletion().getTime();
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
    public List<Order> getOrdersFilteredByDate(Calendar from, Calendar to) {
        return getOrdersFilteredByDate(this.orderRepository.getAll(), from, to);
    }

    @Override
    public List<Order> getOrdersFilteredByDate(List<Order> orders, Calendar from, Calendar to) {
        List<Order> filteredOrders;

        filteredOrders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().compareTo(from) >= 0 && o.getTimeOfCompletion().compareTo(to) <= 0)
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
                .filter(o -> o.getMasters().stream().filter(m -> m.getId().equals(masterId)).findFirst().orElse(null) != null)
                .collect(Collectors.toList());

        return filteredOrders;
    }

}
