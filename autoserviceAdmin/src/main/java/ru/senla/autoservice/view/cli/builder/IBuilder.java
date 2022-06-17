package ru.senla.autoservice.view.cli.builder;

import ru.senla.autoservice.view.cli.menu.Menu;

public interface IBuilder {
    Menu buildMenu();

    Menu getRootMenu();
}
