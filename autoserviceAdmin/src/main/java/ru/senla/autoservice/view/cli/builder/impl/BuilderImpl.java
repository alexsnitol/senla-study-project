package ru.senla.autoservice.view.cli.builder.impl;

import ru.senla.autoservice.view.cli.action.impl.autoserviceaction.PrintNearestFreeDate;
import ru.senla.autoservice.view.cli.action.impl.autoserviceaction.PrintNumberOfFreePlacesByDateActionImpl;
import ru.senla.autoservice.view.cli.builder.impl.garagebuilder.GarageBuilderImpl;
import ru.senla.autoservice.view.cli.builder.impl.masterbuilder.MasterBuilderImpl;
import ru.senla.autoservice.view.cli.builder.impl.orderbuilder.OrderBuilderImpl;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class BuilderImpl extends AbstractBuilderImpl {

    public BuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    public Menu buildMenu() {
        Menu menu = new Menu("Main menu", rootMenu);
        List<MenuItem> menuItems = new ArrayList<>();

        GarageBuilderImpl garageBuilder = new GarageBuilderImpl(menu);
        menuItems.add(new MenuItem("open garage menu", null, garageBuilder.buildMenu()));

        MasterBuilderImpl masterBuilder = new MasterBuilderImpl(menu);
        menuItems.add(new MenuItem("open master menu", null, masterBuilder.buildMenu()));

        OrderBuilderImpl orderBuilder = new OrderBuilderImpl(menu);
        menuItems.add(new MenuItem("open order menu", null, orderBuilder.buildMenu()));

        menuItems.add(new MenuItem("print number of free places on custom date",
                new PrintNumberOfFreePlacesByDateActionImpl(), null));
        menuItems.add(new MenuItem("print nearest free date", new PrintNearestFreeDate(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }

}
