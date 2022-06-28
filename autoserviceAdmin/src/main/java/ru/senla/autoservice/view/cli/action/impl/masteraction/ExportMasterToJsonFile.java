package ru.senla.autoservice.view.cli.action.impl.masteraction;

import ru.senla.autoservice.controller.MasterController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class ExportMasterToJsonFile implements IAction {

    @Override
    public void execute() throws IOException {
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of master:");
        out.print(MenuController.CONSOLE_POINTER);
        Long masterId = scanner.nextLong();

        scanner = new Scanner(System.in);
        out.println("enter filename:");
        out.print(MenuController.CONSOLE_POINTER);
        String fileName = scanner.nextLine();

        try {
            MasterController.getInstance().exportMasterToJsonFile(masterId, fileName);
        } catch (Exception e) {
            err.println(e);
        }
    }

}
