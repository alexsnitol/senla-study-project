package autoservice.view.cli.action.impl.masteraction;


import autoservice.controller.MasterController;
import autoservice.repository.model.Master;
import autoservice.util.IdDistributorUtil;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.util.Scanner;

import static java.lang.System.*;

public class AddNewMasterActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(in);

        String lastName;
        String firstName;
        String patronymic;

        out.println("enter the last name of master");
        out.print(MenuController.CONSOLE_POINTER);
        lastName = scanner.nextLine();
        out.println("enter the first name of master");
        out.print(MenuController.CONSOLE_POINTER);
        firstName = scanner.nextLine();
        out.println("enter the patronymic of master");
        out.print(MenuController.CONSOLE_POINTER);
        patronymic = scanner.nextLine();

        Master newMaster = new Master(lastName, firstName, patronymic);
        newMaster.setId(IdDistributorUtil.getId());

        MasterController.getInstance().add(newMaster);
    }
}
