package autoservice.view.cli.builder.impl.orderbuilder;

import autoservice.view.cli.action.impl.orderaction.*;
import autoservice.view.cli.builder.IBuilder;
import autoservice.view.cli.builder.impl.AbstractBuilderImpl;
import autoservice.view.cli.menu.Menu;
import autoservice.view.cli.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OrderBuilderImpl extends AbstractBuilderImpl implements IBuilder {
    public OrderBuilderImpl(Menu rootMenu) {
        super(rootMenu);
    }

    @Override
    public Menu buildMenu() {
        Menu menu = new Menu("Master menu", rootMenu);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("add new order", new AddNewOrderActionImpl(), null));
        menuItems.add(new MenuItem("delete order", new DeleteOrderActionImpl(), null));
        menuItems.add(new MenuItem("assign master on order", new AssignMasterActionImpl(), null));
        menuItems.add(new MenuItem("remove master on order", new RemoveMasterActionImpl(), null));

        OrderStatusBuilderImpl orderStatusBuilder = new OrderStatusBuilderImpl(menu);
        menuItems.add(new MenuItem("set status the order", null, orderStatusBuilder.buildMenu()));
        menuItems.add(new MenuItem("shift the time of completion the order", new ShiftTimeOfCompletionOrderActionImpl(), null));

        OrderSortBuilderImpl orderSortBuilder = new OrderSortBuilderImpl(menu);
        menuItems.add(new MenuItem("print orders", null, orderSortBuilder.buildMenu()));
        menuItems.add(new MenuItem("print in process order of master", new PrintInProcessOrdersFilteredByMasterActionImpl(), null));

        menu.setMenuItems(menuItems);

        return menu;
    }
}
