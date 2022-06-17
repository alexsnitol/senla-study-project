package ru.senla.autoservice.view.cli.action.impl.autoserviceaction;

import ru.senla.autoservice.util.AutoserviceUtil;
import ru.senla.autoservice.view.cli.action.IAction;

import static java.lang.System.out;

public class PrintNearestFreeDate implements IAction {
    @Override
    public void execute() {
        out.println(AutoserviceUtil.getNearestDate());
    }
}
