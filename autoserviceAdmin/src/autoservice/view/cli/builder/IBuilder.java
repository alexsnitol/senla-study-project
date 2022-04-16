package autoservice.view.cli.builder;

import autoservice.view.cli.menu.Menu;

public interface IBuilder {
    Menu buildMenu();
    Menu getRootMenu();
}
