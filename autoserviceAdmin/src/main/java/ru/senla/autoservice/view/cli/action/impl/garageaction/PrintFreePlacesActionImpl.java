package ru.senla.autoservice.view.cli.action.impl.garageaction;


import ru.senla.autoservice.controller.GarageController;
import ru.senla.autoservice.repository.model.Garage;
import ru.senla.autoservice.view.cli.action.IAction;

import static java.lang.System.out;

public class PrintFreePlacesActionImpl implements IAction {
    @Override
    public void execute() {
        GarageController garageController = GarageController.getInstance();

        for (Garage garage : garageController.getFreePlaces()) {
            out.println("id: " + garage.getId());
//            for (Long place : garage.getPlaces()) {
//                out.println("-- " + place);
//            }
            // TODO
        }
    }
}
