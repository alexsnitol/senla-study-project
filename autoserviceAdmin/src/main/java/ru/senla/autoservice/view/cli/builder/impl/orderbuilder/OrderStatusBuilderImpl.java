package ru.senla.autoservice.view.cli.builder.impl.orderbuilder;

import ru.senla.autoservice.view.cli.action.impl.orderaction.SetCanceledStatusOrderActionImpl;
import ru.senla.autoservice.view.cli.action.impl.orderaction.SetCompletedStatusOrderActionImpl;
import ru.senla.autoservice.view.cli.action.impl.orderaction.SetInProcessStatusOrderActionImpl;
import ru.senla.autoservice.view.cli.action.impl.orderaction.SetPausedStatusOrderActionImpl;
import ru.senla.autoservice.view.cli.action.impl.orderaction.SetPostponedStatusOrderActionImpl;
import ru.senla.autoservice.view.cli.builder.IBuilder;
import ru.senla.autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import ru.senla.autoservice.view.cli.menu.Menu;
import ru.senla.autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusBuilderImpl extends AbstractBuilderImpl implements IBuilder {

    public OrderStatusBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Status order menu", rootMenu);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("set completed", new SetCompletedStatusOrderActionImpl(), null));
        menuItems.add(new MenuItem("set in process", new SetInProcessStatusOrderActionImpl(), null));
        menuItems.add(new MenuItem("set postponed", new SetPostponedStatusOrderActionImpl(), null));
        menuItems.add(new MenuItem("set canceled", new SetCanceledStatusOrderActionImpl(), null));
        menuItems.add(new MenuItem("set paused", new SetPausedStatusOrderActionImpl(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
