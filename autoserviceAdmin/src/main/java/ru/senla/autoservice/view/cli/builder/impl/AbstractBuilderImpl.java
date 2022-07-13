package ru.senla.autoservice.view.cli.builder.impl;

import ru.senla.autoservice.view.cli.builder.IBuilder;
import ru.senla.autoservice.view.cli.menu.Menu;

public abstract class AbstractBuilderImpl implements IBuilder {
    protected Menu rootMenu;

    public AbstractBuilderImpl(Menu rootMenu) {
        this.rootMenu = rootMenu;
    }

    public Menu getRootMenu() {
        return this.rootMenu;
    }
}
