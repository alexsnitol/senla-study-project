package ru.senla.autoservice.view.cli.action.impl.orderaction;


import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.Order;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintInProcessOrdersFilteredByMasterActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        OrderController orderController = OrderController.getInstance();

        out.println("enter id of master");
        out.print(MenuController.CONSOLE_POINTER);
        Long masterId = scanner.nextLong();

        List<Order> orders = orderController.getAllByStatusAndMasterId(OrderStatusEnum.IN_PROCESS, masterId);

        for (Order order : orders) {
            out.println(orderController.getInfoOfOrder(order));
        }
    }
}
