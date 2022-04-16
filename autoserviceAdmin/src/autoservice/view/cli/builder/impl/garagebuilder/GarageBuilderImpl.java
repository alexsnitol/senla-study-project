package autoservice.view.cli.builder.impl.garagebuilder;

import autoservice.view.cli.action.impl.garageaction.*;
import autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import autoservice.view.cli.menu.Menu;
import autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class GarageBuilderImpl extends AbstractBuilderImpl {
    public GarageBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Garage menu", rootMenu);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("add new garage", new AddNewGarageActionImpl(), null));
        menuItems.add(new MenuItem("delete garage", new DeleteGarageActionImpl(), null));
        menuItems.add(new MenuItem("add new place in garage", new AddNewPlaceInGarageActionImpl(), null));
        menuItems.add(new MenuItem("delete last place in garage", new DeleteLastPlaceInGarageActionImpl(), null));
        menuItems.add(new MenuItem("print list of free places",    new PrintFreePlacesActionImpl(),   null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
