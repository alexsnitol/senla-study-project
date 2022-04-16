package autoservice.view.cli.builder.impl;

import autoservice.view.cli.menu.Menu;
import autoservice.view.cli.builder.IBuilder;

public abstract class AbstractBuilderImpl implements IBuilder {
    protected Menu rootMenu;

    public AbstractBuilderImpl(Menu rootMenu) {
        this.rootMenu = rootMenu;
    }

    public Menu getRootMenu() {
        return this.rootMenu;
    }
}
