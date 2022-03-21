import autoservice.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Autoservice myAutoservice = new Autoservice("My Autoservice");

        Garage garage = myAutoservice.getGarage();
        garage.addPlace(5);
        garage.takePlace(1);
        garage.takePlace(4);
        out.println(garage.getInfoOfPlaces());

        out.println();

        MasterArray masters = myAutoservice.getMasters();
        masters.addMaster(new Master("Slotin", "Alexander", "Sergeevich"));
        out.println(masters.getInfoOfMasters());

        out.println();

        OrderArray orders = myAutoservice.getOrders();
        Order tmpOrder = new Order();
        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfCompletion(30);

        Calendar customDate = new GregorianCalendar();
        customDate.add(Calendar.HOUR, 4);
        orders.addOrder(new Order(customDate));

        tmpOrder = new Order();
        orders.addOrder(tmpOrder);
        tmpOrder.setTimeOfCompletion(30);

        customDate = new GregorianCalendar();
        customDate.add(Calendar.DAY_OF_MONTH, 2);
        orders.addOrder(new Order(customDate, OrderStatus.COMPLETED));
        out.println(orders.getInfoOfOrders());

        out.println();

        orders.shiftTimeOfOrders(1, 60);
        out.println(orders.getInfoOfOrders());
    }
}
