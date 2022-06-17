package ru.senla.autoservice.view.cli.action.impl.orderaction;


import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.repository.model.OrderStatusEnum;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.out;

public class SetPostponedStatusOrderActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();
        OrderController.getInstance().setStatus(orderId, OrderStatusEnum.POSTPONED);
    }
}
