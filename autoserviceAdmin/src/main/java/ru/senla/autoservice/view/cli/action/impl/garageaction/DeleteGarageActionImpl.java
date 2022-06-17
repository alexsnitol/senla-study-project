package ru.senla.autoservice.view.cli.action.impl.garageaction;


import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class DeleteGarageActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        out.println("enter id of garage");
        out.print(MenuController.CONSOLE_POINTER);
        Long garageId = scanner.nextLong();

        GarageController.getInstance().deleteById(garageId);
    }
}
