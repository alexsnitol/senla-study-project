package ru.senla.autoservice.view.cli.builder.impl.orderbuilder;

import ru.senla.autoservice.view.cli.action.impl.orderaction.PrintOrdersSortedByDateTimeOfCompletedFilteredByCustomStatusAndByCustomDateTime;
import ru.senla.autoservice.view.cli.action.impl.orderaction.PrintOrdersSortedByDateTimeOfCreatedFilteredByCustomStatusAndByCustomDateTime;
import ru.senla.autoservice.view.cli.action.impl.orderaction.PrintOrdersSortedByPriceFilteredByCustomStatusAndByCustomDateTime;
import ru.senla.autoservice.view.cli.builder.IBuilder;
import ru.senla.autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OrderSortBuilderImpl extends AbstractBuilderImpl implements IBuilder {
    public OrderSortBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Print all orders menu", rootMenu);
        menu.setDescription("Sort setting for print orders");

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("sort by time of created",
                new PrintOrdersSortedByDateTimeOfCreatedFilteredByCustomStatusAndByCustomDateTime(), null));
        menuItems.add(new MenuItem("sort by time of completion",
                new PrintOrdersSortedByDateTimeOfCompletedFilteredByCustomStatusAndByCustomDateTime(), null));
        menuItems.add(new MenuItem("sort by price",
                new PrintOrdersSortedByPriceFilteredByCustomStatusAndByCustomDateTime(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
