package ru.senla.autoservice.view.cli.action.impl.orderaction;

import ru.senla.autoservice.controller.OrderController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class ImportOrderFromJsonFile implements IAction {

    @Override
    public void execute() throws IOException {
        Scanner scanner = new Scanner(System.in);
        out.println("enter path to file:");
        out.print(MenuController.CONSOLE_POINTER);
        String path = scanner.nextLine();

        try {
            OrderController.getInstance().importOrderFromJsonFile(path);
        } catch (Exception e) {
            err.println(e);
        }
    }

}
