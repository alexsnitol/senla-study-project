package ru.senla.autoservice.view.cli.action.impl.masteraction;


import ru.senla.autoservice.controller.MasterController;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintMastersSortedByAlphabetically implements IAction {
    @Override
    public void execute() {
        MasterController masterController = MasterController.getInstance();
        Scanner scanner = new Scanner(System.in);

        out.println("sort order? (a/d)");
        out.print(MenuController.CONSOLE_POINTER);
        char sortOrder = scanner.next().charAt(0);

        List<Master> sortedMasters;

        if (sortOrder == 'd') {
            sortedMasters = masterController.getSorted("AlphabeticallyDesc");
        } else {
            sortedMasters = masterController.getSorted("Alphabetically");
        }

        for (Master master : sortedMasters) {
            out.println(masterController.getFullNameWithId(master));
        }
    }
}
