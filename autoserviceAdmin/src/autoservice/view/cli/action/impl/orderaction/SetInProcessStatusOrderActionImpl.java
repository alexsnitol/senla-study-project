package autoservice.view.cli.action.impl.orderaction;

import autoservice.controller.AutoserviceController;
import autoservice.repository.model.OrderStatusEnum;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.out;

public class SetInProcessStatusOrderActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();
        AutoserviceController.getOrderController().setStatus(orderId, OrderStatusEnum.IN_PROCESS);
    }
}
