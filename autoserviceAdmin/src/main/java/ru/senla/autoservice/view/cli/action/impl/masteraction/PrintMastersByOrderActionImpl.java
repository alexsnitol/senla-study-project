package ru.senla.autoservice.view.cli.action.impl.masteraction;


import ru.senla.autoservice.controller.MasterController;
import ru.senla.autoservice.repository.model.Master;
import ru.senla.autoservice.view.cli.MenuController;
import ru.senla.autoservice.view.cli.action.IAction;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintMastersByOrderActionImpl implements IAction {
    @Override
    public void execute() {
        MasterController masterController = MasterController.getInstance();
        Scanner scanner = new Scanner(System.in);

        out.println("enter id of order");
        out.print(MenuController.CONSOLE_POINTER);
        Long orderId = scanner.nextLong();

        List<Master> mastersOfOrder = masterController.getMastersByOrder(orderId);

        for (Master master : mastersOfOrder) {
            out.println(masterController.getFullNameWithId(master));
        }
    }
}
