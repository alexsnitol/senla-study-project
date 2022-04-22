package autoservice.view.cli.action.impl.autoserviceaction;

import autoservice.util.AutoserviceUtil;
import autoservice.view.cli.MenuController;
import autoservice.view.cli.action.IAction;

import java.time.LocalDate;
import java.util.Scanner;

import static java.lang.System.out;

public class PrintNumberOfFreePlacesByDateActionImpl implements IAction {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        out.println("enter date with a space (year month day)");
        out.print(MenuController.CONSOLE_POINTER);
        int year = scanner.nextInt();
        int month = scanner.nextInt();
        int day = scanner.nextInt();

        out.println(AutoserviceUtil.getNumberOfFreePlacesByDate(LocalDate.of(year, month, day)));
    }
}
