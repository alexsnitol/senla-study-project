package autoservice.view.cli.action.impl.garageaction;

import autoservice.controller.AutoserviceController;
import autoservice.controller.GarageController;
import autoservice.repository.model.Garage;
import autoservice.view.cli.action.IAction;

import static java.lang.System.*;

public class PrintFreePlacesActionImpl implements IAction {
    @Override
    public void execute() {
        GarageController garageController = AutoserviceController.getGarageController();

        for (Garage garage : garageController.getFreePlaces()) {
            out.println("id: " + garage.getId());
            for (Long place : garage.getPlaces()) {
                out.println("-- " + place);
            }
        }
    }
}
