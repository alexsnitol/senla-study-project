package autoservice.view.cli.action;

import java.io.IOException;

@FunctionalInterface
public interface IAction {
    void execute() throws IOException;
}
