package ru.senla.autoservice.view.cli.action.impl.orderaction;


import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintOrdersSortedByDateTimeOfCompletedFilteredByCustomStatusAndByCustomDateTime implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        OrderController orderController = OrderController.getInstance();

        out.println("filter by status? (y/n)");
        out.print(MenuController.CONSOLE_POINTER);
        char filter = scanner.next().charAt(0);

        OrderStatusEnum orderStatus = null;

        if (filter == 'y') {
            out.println("enter index of order status");

            for (int i = 0; i < OrderStatusEnum.values().length; i++) {
                out.println(i + 1 + ". " + OrderStatusEnum.values()[i].toString().toLowerCase());
            }

            out.print(MenuController.CONSOLE_POINTER);
            int statusIndex = scanner.nextInt();

            orderStatus = OrderStatusEnum.values()[statusIndex - 1];
        }

        out.println("sort order? (a/d)");
        out.print(MenuController.CONSOLE_POINTER);
        char sortOrder = scanner.next().charAt(0);

        List<Order> resultOrders;

        if (filter == 'y') {
            resultOrders = orderController.getAllByStatusSorted(orderStatus, "TimeOfCompletion");
        } else {
            if (sortOrder == 'd') {
                resultOrders = orderController.getSorted("TimeOfCompletionDesc");
            } else {
                resultOrders = orderController.getSorted("TimeOfCompletion");
            }
        }

        for (Order order : resultOrders) {
            out.println(orderController.getInfoOfOrder(order));
        }
    }
}
