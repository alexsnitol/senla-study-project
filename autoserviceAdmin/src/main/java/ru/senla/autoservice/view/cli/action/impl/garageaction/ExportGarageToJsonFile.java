package ru.senla.autoservice.view.cli.action.impl.garageaction;

import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class ExportGarageToJsonFile implements IAction {

    @Override
    public void execute() throws IOException {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of garage:");
        out.print(MenuController.CONSOLE_POINTER);
        Long garageId = scanner.nextLong();

        scanner = new Scanner(System.in);
        out.println("enter filename:");
        out.print(MenuController.CONSOLE_POINTER);
        String fileName = scanner.nextLine();

        try {
            GarageController.getInstance().exportGarageToJsonFile(garageId, fileName);
        } catch (Exception e) {
            err.println(e);
        }
    }

}
