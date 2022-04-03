package autoservice.Repositories;

import java.util.*;
import java.util.stream.Collectors;

public class OrderRepository {

    private ArrayList<Order> orders = new ArrayList<>();
    private int lastId = -1;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = (ArrayList<Order>) orders;
    }

    public void addOrder(Order order) {
        this.lastId++;
        order.setId(lastId);
        this.orders.add(order);
    }

    public void deleteOrder(int index) {
        if (index >= 0 && index < orders.size())
            this.orders.get(index).setStatus(OrderStatus.DELETED);
    }

    public void deleteOrderById(int id) {
        for (Order order : this.orders) {
            if (order.getId() == id) {
                order.setStatus(OrderStatus.DELETED);
                return;
            }
        }
    }

    public Order getOrder(int index) {
        if (index >= 0 && index < orders.size())
            return this.orders.get(index);
        return null;
    }

    public Order getOrderById(int id) {
        for (Order order : this.orders) {
            if (order.getId() == id)
                return order;
        }

        return null;
    }

    public int sizeOrders() {
        return this.orders.size();
    }

    public String getInfoOfOrders() {
        String result = "";
        for (int i = 0; i < orders.size(); i++) {
            result += "" + (i + 1) + ". " + orders.get(i).toString();
            if (i != orders.size() - 1)
                result += "\n";
        }
        return result;
    }

    public OrderRepository getOrdersSortedByTimeOfCreated() {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .sorted(Comparator.comparing(Order::getTimeOfCreated))
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersSortedByTimeOfBegin() {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .sorted(Comparator.comparing(Order::getTimeOfBegin))
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersSortedByTimeOfCompletion() {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .sorted(Comparator.comparing(Order::getTimeOfCompletion))
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersSortedByPrice() {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .sorted(Comparator.comparing(Order::getPrice))
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersFilteredByDate(Calendar from, Calendar to) {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .filter(o -> o.getTimeOfCompletion().compareTo(from) >= 0 && o.getTimeOfCompletion().compareTo(to) <= 0)
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersFilteredByStatus(OrderStatus status) {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }

    public OrderRepository getOrdersFilteredByMaster(int masterId) {
        OrderRepository orderRepository = new OrderRepository();
        List<Order> orders = (ArrayList<Order>) this.orders.clone();

        orders = orders.stream()
                .filter(o -> o.getMasters().getMasterById(masterId) != null)
                .collect(Collectors.toList());

        orderRepository.setOrders(orders);

        return orderRepository;
    }
}