package autoservice.view.cli.action.impl.autoserviceaction;

import autoservice.util.AutoserviceUtil;
import autoservice.view.cli.action.IAction;

import static java.lang.System.out;

public class PrintNearestFreeDate implements IAction {
    @Override
    public void execute() {
        out.println(AutoserviceUtil.getNearestDate());
    }
}
