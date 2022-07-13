package ru.senla.autoservice.view.cli.action.impl.garageaction;

import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class ImportGarageFromJsonFile implements IAction {

    @Override
    public void execute() throws IOException {
        Scanner scanner = new Scanner(System.in);
        out.println("enter path to file:");
        out.print(MenuController.CONSOLE_POINTER);
        String path = scanner.nextLine();

        try {
            GarageController.getInstance().importGarageFromJsonFile(path);
        } catch (Exception e) {
            err.println(e);
        }
    }

}
