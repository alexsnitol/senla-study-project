import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        Autoservice myAutoservice = new Autoservice();
        OrderStatus orderStatus = new OrderStatus();

        Garage garage = myAutoservice.getGarage();
        garage.addPlace(5);
        garage.takePlace(1);
        garage.takePlace(4);
        garage.printPlaces();

        System.out.println();

        MasterArray masters = myAutoservice.getMasters();
        masters.addMaster(new Master("Slotin", "Alexander", "Sergeevich"));
        masters.printMasters();

        System.out.println();

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
        orders.addOrder(new Order(customDate, orderStatus.getStatus(0), false));
        orders.printOrders();

        System.out.println();

        orders.shiftTimeOfOrders(1, 60);
        orders.printOrders();
    }
}
