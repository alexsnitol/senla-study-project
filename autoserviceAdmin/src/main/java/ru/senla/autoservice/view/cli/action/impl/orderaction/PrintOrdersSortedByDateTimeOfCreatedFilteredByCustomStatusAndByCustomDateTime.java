package ru.senla.autoservice.view.cli.action.impl.orderaction;


import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintOrdersSortedByDateTimeOfCreatedFilteredByCustomStatusAndByCustomDateTime implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        OrderController orderController = OrderController.getInstance();
        List<Order> orders = OrderController.getInstance().getAll();

        out.println("filter by status? (y/n)");
        out.print(MenuController.CONSOLE_POINTER);

        if (scanner.next().charAt(0) == 'y') {
            out.println("enter index of order status");

            for (int i = 0; i < OrderStatusEnum.values().length; i++) {
                out.println(i + 1 + ". " + OrderStatusEnum.values()[i].toString().toLowerCase());
            }

            out.print(MenuController.CONSOLE_POINTER);
            int statusIndex = scanner.nextInt();

            OrderStatusEnum orderStatus = OrderStatusEnum.values()[statusIndex];

            orders = orderController.getOrdersFilteredByStatus(orderStatus);
        }

        out.println("sort order? (a/d)");
        out.print(MenuController.CONSOLE_POINTER);
        char sortOrder = scanner.next().charAt(0);

        List<Order> resultOrders = orderController.getSorted(orders, "DateTimeOfCreated");

        if (sortOrder == 'd') {
            Collections.reverse(resultOrders);
        }

        for (Order order : resultOrders) {
            out.println(orderController.getInfoOfOrder(order));
        }
    }
}
