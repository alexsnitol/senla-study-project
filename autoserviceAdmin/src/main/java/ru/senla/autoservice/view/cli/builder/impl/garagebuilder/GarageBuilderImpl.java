package ru.senla.autoservice.view.cli.builder.impl.garagebuilder;

import ru.senla.autoservice.view.cli.action.impl.garageaction.AddNewGarageActionImpl;
import ru.senla.autoservice.view.cli.action.impl.garageaction.AddNewPlaceInGarageActionImpl;
import ru.senla.autoservice.view.cli.action.impl.garageaction.DeleteGarageActionImpl;
import ru.senla.autoservice.view.cli.action.impl.garageaction.DeleteLastPlaceInGarageActionImpl;
import ru.senla.autoservice.view.cli.action.impl.garageaction.ExportGarageToJsonFile;
import ru.senla.autoservice.view.cli.action.impl.garageaction.ImportGarageFromJsonFile;
import ru.senla.autoservice.view.cli.action.impl.garageaction.PrintFreePlacesActionImpl;
import ru.senla.autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

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
        menuItems.add(new MenuItem("print list of free places", new PrintFreePlacesActionImpl(), null));
        menuItems.add(new MenuItem("export garage to json file", new ExportGarageToJsonFile(), null));
        menuItems.add(new MenuItem("import garage from json file", new ImportGarageFromJsonFile(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
