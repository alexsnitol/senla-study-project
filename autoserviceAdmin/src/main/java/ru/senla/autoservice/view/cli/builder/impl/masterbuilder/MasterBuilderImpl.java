package ru.senla.autoservice.view.cli.builder.impl.masterbuilder;

import ru.senla.autoservice.view.cli.action.impl.masteraction.AddNewMasterActionImpl;
import ru.senla.autoservice.view.cli.action.impl.masteraction.DeleteMasterActionImpl;
import ru.senla.autoservice.view.cli.action.impl.masteraction.ExportMasterToJsonFile;
import ru.senla.autoservice.view.cli.action.impl.masteraction.ImportMasterFromJsonFile;
import ru.senla.autoservice.view.cli.action.impl.masteraction.PrintMastersByOrderActionImpl;
import ru.senla.autoservice.view.cli.builder.IBuilder;
import ru.senla.autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MasterBuilderImpl extends AbstractBuilderImpl implements IBuilder {
    public MasterBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Master menu", rootMenu);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("add new master", new AddNewMasterActionImpl(), null));
        menuItems.add(new MenuItem("delete master", new DeleteMasterActionImpl(), null));

        MasterSortBuilderImpl masterSortBuilder = new MasterSortBuilderImpl(menu);
        menuItems.add(new MenuItem("print masters", null, masterSortBuilder.buildMenu()));

        menuItems.add(new MenuItem("print masters by order", new PrintMastersByOrderActionImpl(), null));
        menuItems.add(new MenuItem("export master to json file", new ExportMasterToJsonFile(), null));
        menuItems.add(new MenuItem("import master from json file", new ImportMasterFromJsonFile(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
