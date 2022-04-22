package autoservice.view.cli.action.impl.garageaction;


import autoservice.controller.GarageController;
import autoservice.repository.model.Garage;
import autoservice.util.IdDistributorUtil;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.*;

public class AddNewGarageActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        out.println("enter size of garage");
        out.print(MenuController.CONSOLE_POINTER);
        int size = scanner.nextInt();

        Garage newGarage = new Garage(size);
        newGarage.setId(IdDistributorUtil.getId());
        GarageController.getInstance().add(newGarage);
    }
}