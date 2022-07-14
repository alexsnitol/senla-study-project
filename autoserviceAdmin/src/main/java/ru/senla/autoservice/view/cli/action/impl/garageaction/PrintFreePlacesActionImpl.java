package ru.senla.autoservice.view.cli.action.impl.garageaction;


import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.List;

import static java.lang.System.out;

public class PrintFreePlacesActionImpl implements IAction {
    @Override
    public void execute() {
        GarageController garageController = GarageController.getInstance();

        for (List<Long> places : garageController.getFreePlaces()) {
            out.println("id: " + places.get(0));
            for (int i = 1; i < places.size(); i++) {
                out.println("-- " + places.get(i));
            }
        }
    }
}
