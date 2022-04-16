package autoservice.view.cli.action.impl.autoserviceaction;

import autoservice.controller.AutoserviceController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.out;

public class PrintNearestFreeDate implements IAction {
    @Override
    public void execute() {
        out.println(AutoserviceController.getInstance().getNearestDate().getTime());
    }
}
