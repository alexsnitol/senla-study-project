package ru.senla.autoservice.view.cli.action;

@FunctionalInterface
public interface IAction {
    void execute() throws Exception;
}
