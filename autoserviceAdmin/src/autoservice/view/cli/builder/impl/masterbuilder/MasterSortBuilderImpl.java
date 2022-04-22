package autoservice.view.cli.builder.impl.masterbuilder;

import autoservice.view.cli.action.impl.masteraction.PrintMastersSortedByAlphabetically;
import autoservice.view.cli.action.impl.masteraction.PrintMastersSortedByNumberOfActiveOrders;
import autoservice.view.cli.builder.IBuilder;
import autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import autoservice.view.cli.menu.Menu;
import autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MasterSortBuilderImpl extends AbstractBuilderImpl implements IBuilder {

    public MasterSortBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Print masters menu", rootMenu);
        menu.setDescription("Sort setting for print masters");

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("sort by alphabetically", new PrintMastersSortedByAlphabetically(), null));
        menuItems.add(new MenuItem("sort by number of active orders", new PrintMastersSortedByNumberOfActiveOrders(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}