package autoservice.view.cli.action.impl.orderaction;


import autoservice.controller.OrderController;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.out;

public class AssignMasterActionImpl implements IAction {

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();

        out.println("enter id of master");
        out.print(MenuController.CONSOLE_POINTER);
        Long masterId = scanner.nextLong();

        OrderController.getInstance().assignMasterById(orderId, masterId);
    }
}
