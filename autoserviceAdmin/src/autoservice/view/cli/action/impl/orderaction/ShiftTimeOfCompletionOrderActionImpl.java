package autoservice.view.cli.action.impl.orderaction;


import autoservice.controller.OrderController;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.out;

public class ShiftTimeOfCompletionOrderActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();

        out.println("enter shift minutes");
        out.print(MenuController.CONSOLE_POINTER);
        int minutes = scanner.nextInt();

        OrderController.getInstance().shiftTimeOfCompletion(orderId, minutes);
    }
}