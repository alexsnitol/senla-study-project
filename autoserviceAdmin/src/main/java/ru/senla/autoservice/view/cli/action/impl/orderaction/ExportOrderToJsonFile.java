package ru.senla.autoservice.view.cli.action.impl.orderaction;

import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class ExportOrderToJsonFile implements IAction {

    @Override
    public void execute() throws IOException {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order:");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();

        scanner = new Scanner(System.in);
        out.println("enter filename:");
        out.print(MenuController.CONSOLE_POINTER);
        String fileName = scanner.nextLine();

        try {
            OrderController.getInstance().exportOrderToJsonFile(orderId, fileName);
        } catch (Exception e) {
            err.println(e);
        }
    }

}
