package autoservice.view.cli.builder.impl.masterbuilder;

import autoservice.view.cli.action.impl.masteraction.AddNewMasterActionImpl;
import autoservice.view.cli.action.impl.masteraction.DeleteMasterActionImpl;
import autoservice.view.cli.action.impl.masteraction.PrintMastersByOrderActionImpl;
import autoservice.view.cli.builder.IBuilder;
import autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import autoservice.view.cli.menu.Menu;
import autoservice.view.cli.menu.MenuItem;

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

        menu.setMenuItems(menuItems);

        return menu;
    }
}
