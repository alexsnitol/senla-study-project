package autoservice.view.cli.action.impl.garageaction;

import autoservice.controller.AutoserviceController;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class AddNewPlaceInGarageActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        out.println("enter id of garage");
        out.print(MenuController.CONSOLE_POINTER);
        Long garageId = scanner.nextLong();

        AutoserviceController.getGarageController().addPlace(garageId);
    }
}
