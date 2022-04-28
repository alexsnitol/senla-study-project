package autoservice.view.cli.action.impl.masteraction;


import autoservice.controller.MasterController;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class DeleteMasterActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        out.println("enter id of master");
        out.print(MenuController.CONSOLE_POINTER);
        Long masterId = scanner.nextLong();

        MasterController.getInstance().deleteById(masterId);
    }
}
