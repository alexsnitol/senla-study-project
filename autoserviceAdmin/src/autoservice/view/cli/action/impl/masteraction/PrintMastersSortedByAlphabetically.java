package autoservice.view.cli.action.impl.masteraction;

import autoservice.controller.AutoserviceController;
import autoservice.controller.MasterController;
import autoservice.repository.model.Master;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintMastersSortedByAlphabetically implements IAction {
    @Override
    public void execute() {
        MasterController masterController = AutoserviceController.getMasterController();
        Scanner scanner = new Scanner(System.in);

        out.println("sort order? (a/d)");
        out.print(MenuController.CONSOLE_POINTER);
        char sortOrder = scanner.next().charAt(0);

        List<Master> sortedMasters = masterController.getSorted("Alphabetically");

        if (sortOrder == 'd') {
            Collections.reverse(sortedMasters);
        }

        for (Master master : sortedMasters) {
            out.println(masterController.getFullNameWithId(master));
        }
    }
}
