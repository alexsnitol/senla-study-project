package ru.senla.autoservice.view.cli.action.impl.garageaction;


import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class AddNewGarageActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        out.println("enter size of garage");
        out.print(MenuController.CONSOLE_POINTER);
        int size = scanner.nextInt();

        Garage newGarage = new Garage(size);
        GarageController.getInstance().add(newGarage);
    }
}
